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

    private void increment_token(){
        token_index++;
        current_token = this.eot() ? null : this.tokens.get(token_index);
    }

    private void increment_index(){
        char_index++;
        current_char = this.eof() ? null : json_source.charAt(char_index);
    }

    private static boolean is_whitespace(char c){
        for(char ws: WHITESPACE){
            if(ws == c) return true;
        }
        return false;
    }

    private static boolean is_token_char(char c){
        for(char tk: TOKEN_CHARS){
            if(tk == c) return true;
        }
        return false;
    }

    private JSONToken get_next_token() throws JSONUnfinishedStringAtEOF {

        // Handle being at the end of source
        if(this.eof()){
            return JSONToken.generateEOFToken();
        }

        // Read all whitespace before token
        while(is_whitespace(current_char)){
            this.increment_index();
        }

        StringBuilder token_sb = new StringBuilder();

        if(current_char == QUOTE_CHAR){
           do {
                token_sb.append(current_char);
                this.increment_index();
                if(this.eof()){
                    throw new JSONUnfinishedStringAtEOF();
                }
            }  while(current_char != QUOTE_CHAR);
            token_sb.append(current_char);
            this.increment_index();
        }
        else if(is_token_char(current_char)){
            String token_string = new String(new char[]{current_char});
            this.increment_index();
            return new JSONToken(token_string);
        }
        else{
            do {
                token_sb.append(current_char);
                this.increment_index();
            }  while(!is_whitespace(current_char) && !is_token_char(current_char) && !this.eof());
        }

        return new JSONToken(token_sb.toString());
    }

    private void getTokens() throws JSONUnfinishedStringAtEOF{
        while(char_index < json_source.length()){
            JSONToken token = this.get_next_token();
            tokens.add(token);
        }
    }

    private JSONValue get_array() throws JSONMalformedSourceException{
        JSONArray array = new JSONArray();
        JSONValue new_value;

        increment_token();
        if(current_token.type == TOKEN_TYPE.END_ARRAY){
            return new JSONValue(array);
        }

        new_value = get_value();
        System.out.println("got " + new_value);
        array.add(new_value);

        while(!this.eot() && current_token.type != TOKEN_TYPE.END_ARRAY){
            if(current_token.type == TOKEN_TYPE.ELEMENT_DELIMITER){
                increment_token();
                new_value = get_value();
                System.out.println("got " + new_value);
                array.add(new_value);
            }
            else {
                throw new MalformedParameterizedTypeException(
                        "Expected an element separator between values in Array, found "
                            + current_token.value.toString()
                );
            }

        }

        return new JSONValue(array);
    }

    private void add_key_value(JSONObject object) throws JSONMalformedSourceException {
        // Process Key name
        if(current_token.type != TOKEN_TYPE.STRING_LITERAL){
            throw new JSONMalformedSourceException(
                    "String expected in place of key. Found '" + current_token.value + "'");
        }
        String key = (String) current_token.value;
        increment_token();

        // Process delimiter (colon)
        if(current_token.type != TOKEN_TYPE.KEY_VALUE_DELIMITER){
            throw new JSONMalformedSourceException(
                    "Expeted ':' (colon) between key and value. Found '" + current_token.value + "'");
        }
        increment_token();

        // Process value
        JSONValue value = get_value();
        object.put(key, value);

        System.out.println("got " + key + " : " + value.toString());

    }

    /**
     * Processes a JSON object starting at the currently processed token.
     * Utilizes the add_key_value function to process key-value pairs.
     * Expects the token_index to point at a START_OBJECT token ('{').
     * After running the current token points at the END_ARRAY token ('}').
     *
     * @return The JSON object wrapped in a JSONValue.
     * @throws JSONMalformedSourceException When the object does not conform
     * to the grammar or an EOT is found during processing.
     */
    private JSONValue get_object() throws JSONMalformedSourceException{

        // The object to be returned
        JSONObject object = new JSONObject();

        // token_index now points at START_OBJECT
        increment_token();

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
        add_key_value(object);

        // Step 3.)
        while(!this.eot() && current_token.type != TOKEN_TYPE.END_OBJECT){
            if(current_token.type == TOKEN_TYPE.ELEMENT_DELIMITER){
                increment_token();
                add_key_value(object);
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
    private JSONValue get_value() throws JSONMalformedSourceException {

        JSONToken current_token = tokens.get(token_index);

        // Check if the token is a value and return appropriate JSONValue
        JSONValue value;
        switch(current_token.type) {
            case STRING_LITERAL ->  value = new JSONValue((String) current_token.value);
            case NUMBER ->          value = new JSONValue((Double) current_token.value);
            case BOOLEAN ->         value = new JSONValue((boolean) current_token.value);
            case NULL ->            value = new JSONValue();
            case START_ARRAY ->     value = get_array();
            case START_OBJECT ->    value = get_object();
            default -> {
                // The token is not value -> Throw
                throw new JSONMalformedSourceException(
                        "Expected a value, provided " + current_token.value.toString());
            }
        }

        increment_token();
        return value;
    }

    // Pridat streamove citanie?
    public JSONValue parseString() throws JSONMalformedSourceException, JSONUnfinishedStringAtEOF {

        // Tokenize the input string
        this.getTokens();

        // All JSON inputs must contain only one value
        return get_value();
    }
}
