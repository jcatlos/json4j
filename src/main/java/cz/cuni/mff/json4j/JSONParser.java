package cz.cuni.mff.json4j;

import java.lang.reflect.MalformedParameterizedTypeException;
import java.util.ArrayList;

import org.apache.commons.lang.StringEscapeUtils;


public class JSONParser {

    int char_index;
    Character current_char;
    String json_source;

    ArrayList<JSONToken> tokens;
    int token_index;
    JSONToken current_token;

    // CHARACTER CONSTANTS
    final static char QUOTE_CHAR = '"';
    final static char[] WHITESPACE = {' ', '\t', '\n', '\r'};
    final static char[] TOKEN_CHARS = {'{', '}', '[', ']', ',', ':'};

    public JSONParser(String source_string){

        // Handle all unicode character sequences in the source
        json_source = StringEscapeUtils.unescapeJava(source_string.trim());

        // Initialize reading index and current_char at the beginning
        char_index = 0;
        if(json_source.length() > 0){
            current_char = json_source.charAt(0);
        }

        token_index = 0;
        tokens = new ArrayList<>();
    }

    private boolean eof(){
        return char_index >= json_source.length();
    }

    private boolean eot(){
        return token_index >= tokens.size();
    }

    private void incrementToken(){
        token_index++;
        current_token = this.eot() ? null : this.tokens.get(token_index);
    }

    private void incrementIndex(){
        char_index++;
        current_char = this.eof() ? null : json_source.charAt(char_index);
    }

    private static boolean isWhitespace(char c){
        for(char ws: WHITESPACE){
            if(ws == c) return true;
        }
        return false;
    }

    private static boolean isTokenChar(char c){
        for(char tk: TOKEN_CHARS){
            if(tk == c) return true;
        }
        return false;
    }

    private JSONToken getNextToken() throws JSONUnfinishedStringAtEOF {

        // Handle being at the end of source
        if(this.eof()){
            return JSONToken.generateEOFToken();
        }

        // Read all whitespace before token
        while(isWhitespace(current_char)){
            this.incrementIndex();
        }

        StringBuilder token_sb = new StringBuilder();

        if(current_char == QUOTE_CHAR){
           do {
                token_sb.append(current_char);
                this.incrementIndex();
                if(this.eof()){
                    throw new JSONUnfinishedStringAtEOF();
                }
            }  while(current_char != QUOTE_CHAR);
            token_sb.append(current_char);
            this.incrementIndex();
        }
        else if(isTokenChar(current_char)){
            String token_string = new String(new char[]{current_char});
            this.incrementIndex();
            return new JSONToken(token_string);
        }
        else{
            do {
                token_sb.append(current_char);
                this.incrementIndex();
            }  while(!isWhitespace(current_char) && !isTokenChar(current_char) && !this.eof());
        }

        return new JSONToken(token_sb.toString());
    }

    /**
     * Fetches all tokens of the input into this.tokens().
     * Calls get_next_token() until the char_index is at the end of input.
     *
     * @throws JSONUnfinishedStringAtEOF If the input ends at unfinished String token.
     */
    private void getTokens() throws JSONUnfinishedStringAtEOF{
        while(char_index < json_source.length()){
            JSONToken token = this.getNextToken();
            tokens.add(token);
        }
    }

    /**
     * Processes a JSON array starting at the currently processed token.
     * Expects the token_index to point at a START_ARRAY token ('[').
     * After running the current token points at the END_ARRAY token (']').
     *
     * @return The JSON Array wrapped in a JSONValue.
     * @throws JSONMalformedSourceException When the array does not conform
     * to the grammar or an EOT is found during processing.
     */
    private JSONValue getArray() throws JSONMalformedSourceException{

        // The Array to be returned
        JSONArray array = new JSONArray();

        // token_index now points at START_OBJECT
        incrementToken();

        // To deal with no trailing comma, we do these steps:
        // 1.) If the array contains no values, return empty array
        // 2.) Read a value WITHOUT comma after it
        // 3.) Until the end of the array, we will read in order
        //     COMMA, VALUE

        // Step 1.)
        if(current_token.type == TOKEN_TYPE.END_ARRAY){
            return new JSONValue(array);
        }

        //Step 2.)
        array.add(getValue());

        // Step 3.)
        while(!this.eot() && current_token.type != TOKEN_TYPE.END_ARRAY){
            if(current_token.type == TOKEN_TYPE.ELEMENT_DELIMITER){
                incrementToken();
                array.add(getValue());
            }
            else {
                throw new MalformedParameterizedTypeException(
                        "Expected an element separator between values in Array, found "
                            + current_token.value.toString()
                );
            }
        }

        // If the construction ended by finding EOT, we throw
            // No closing bracket found
        if(this.eot()){
            throw new MalformedParameterizedTypeException(
                    "Unexpected end of file during Array construction"
            );
        }

        return new JSONValue(array);
    }

    /**
     * Processes a key-value pair and adds it to the JSONObject.
     * The key must be a string and key and value must be separated by a colon (:).
     *
     * @param object The JSONObject to which the pair should be added.
     * @throws JSONMalformedSourceException If the pair is not properly formed.
     */
    private void addKeyValue(JSONObject object) throws JSONMalformedSourceException {

        // Process Key name
        if(current_token.type != TOKEN_TYPE.STRING_LITERAL){
            throw new JSONMalformedSourceException(
                    String.format(
                        "String expected in place of key. Found '%s'",
                        current_token.value.toString()
                    )
            );
        }
        String key = (String) current_token.value;
        incrementToken();

        // Process delimiter (colon)
        if(current_token.type != TOKEN_TYPE.KEY_VALUE_DELIMITER){
            throw new JSONMalformedSourceException(
                String.format(
                    "Expeted ':' (colon) between key and value. Found '%s'",
                    current_token.value.toString()
                )
            );
        }
        incrementToken();

        // Process value
        object.put(key, getValue());
    }

    /**
     * Processes a JSON object starting at the currently processed token.
     * Utilizes the addKeyValue function to process key-value pairs.
     * Expects the token_index to point at a START_OBJECT token ('{').
     * After running the current token points at the END_ARRAY token ('}').
     *
     * @return The JSON object wrapped in a JSONValue.
     * @throws JSONMalformedSourceException When the object does not conform
     * to the grammar or an EOT is found during processing.
     */
    private JSONValue getObject() throws JSONMalformedSourceException{

        // The object to be returned
        JSONObject object = new JSONObject();

        // token_index now points at START_OBJECT
        incrementToken();

        // To deal with no trailing comma, we do these steps:
            // 1.) If the object contains no values, return empty object
            // 2.) Read a value WITHOUT comma after it
            // 3.) Until the end of the object, we will read in order
            //     COMMA, KEY, COLON, VALUE

        // Step 1.)
        if(current_token.type == TOKEN_TYPE.END_OBJECT){
            return new JSONValue(object);
        }

        // Step 2.)
        addKeyValue(object);

        // Step 3.)
        while(!this.eot() && current_token.type != TOKEN_TYPE.END_OBJECT){
            if(current_token.type == TOKEN_TYPE.ELEMENT_DELIMITER){
                incrementToken();
                addKeyValue(object);
            }
            else {
                throw new MalformedParameterizedTypeException(
                        "Expected an element separator between values in Object, found "
                        + current_token.value.toString()
                );
            }
        }

        // If the construction ended by finding EOT, we throw
            // No closing bracket found
        if(this.eot()){
            throw new MalformedParameterizedTypeException(
                    "Unexpected end of file during Object construction"
            );
        }

        return new JSONValue(object);
    }

    /**
     * Fetches a value starting at the currently processed token.
     * Expects the token_index to be at a JSON value.
     * If the value is not an Object or Array, only one token is processed.
     * Otherwise all tokens until next END_OBJECT / END_ARRAY are processed.
     * DOES NOT move the token_index behind the value (for compound values
     * the index points at the last token of the value).
     *
     * @return JSONValue of the current token
     * @throws JSONMalformedSourceException If the current token is not a value
     */
    private JSONValue getValue() throws JSONMalformedSourceException {

        JSONToken current_token = tokens.get(token_index);

        // Check if the token is a value and return appropriate JSONValue
        JSONValue value;
        switch(current_token.type) {
            case STRING_LITERAL ->  value = new JSONValue((String) current_token.value);
            case NUMBER ->          value = new JSONValue((Double) current_token.value);
            case BOOLEAN ->         value = new JSONValue((boolean) current_token.value);
            case NULL ->            value = new JSONValue();
            case START_ARRAY ->     value = getArray();
            case START_OBJECT ->    value = getObject();
            default -> {
                // The token is not value -> Throw
                throw new JSONMalformedSourceException(
                    "Expected a value, provided " + current_token.value.toString())
                ;
            }
        }

        incrementToken();
        return value;
    }

    // Pridat streamove citanie?
    public JSONValue parseString() throws JSONMalformedSourceException, JSONUnfinishedStringAtEOF {

        // Tokenize the input string
        this.getTokens();

        // All JSON inputs must contain only one value
        return getValue();
    }
}
