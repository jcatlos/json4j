package cz.cuni.mff.json4j;

/**
 * Enumeration of the JSONToken types.
 * Not public, since there is no reason to use it outside parser implementation.
 */
enum TOKEN_TYPE {
    START_OBJECT,           // '{'
    END_OBJECT,             // '}'
    START_ARRAY,            // '['
    END_ARRAY,              // ']'
    ELEMENT_DELIMITER,      // ','
    KEY_VALUE_DELIMITER,    // ':'
    STRING_LITERAL,         // A string enclosed in quotes ('"')
    NUMBER,                 // Any number that fits to double
    BOOLEAN,                // "true" or "false" (without the quotes)
    NULL,                   // "null" (without the quotes)
    EOF,                    // The token signifying the end of file
    INVALID_TOKEN;          // Anything other is evaluated as invalid
}


/**
 * Implementation of a lexical token for the purposes of the JSONParser.
 * Holds the token's type and also its value. That makes sense only for values,
 * so for other token types there are sensible defaults chosen.
 * Not public, since there is no reason to use it outside parser implementation.
 */

class JSONToken {

    // CHARACTER CONSTANTS
    private final static String KEY_VALUE_DELIMITER = ":";
    private final static String ELEMENT_DELIMITER   = ",";

    private final static String START_OBJECT  = "{";
    private final static String END_OBJECT    = "}";
    private final static String START_ARRAY   = "[";
    private final static String END_ARRAY     = "]";

    /**
     * Type of the token.
     * Used to determine the type of the value it holds.
     */
    public final TOKEN_TYPE type;

    /**
     * Value the token holds.
     * Needs to be casted to the proper type when used. Use this.type to determine it.
     */
    public final Object value;


    /**
     * Creates an EOF token.
     * There is no sensible way of recognizing an EOF token based on its value
     * and all moments when an EOF token should be emitted are explicitly known,
     * creating a function to produce an EOF token is the most elegant solution.
     * @return A token with type TOKEN_TYPE.EOF and value = null.
     */
    public static JSONToken generateEOFToken(){
        return new JSONToken(TOKEN_TYPE.EOF, null);
    }

    /**
     * Creates a token with specified fields.
     * Used only in generateEOFToken() and therefore private.
     * @param type TOKEN_TYPE of the token.
     * @param value value of the token.
     */
    private JSONToken(TOKEN_TYPE type, Object value){
        this.type = type;
        this.value = value;
    }

    /**
     * Creates an appropriate token based on the provided value.
     * Thanks to the nature of the JSON grammar, the token type can be determined
     * solely (except for EOF) based on the textual value (no context required).
     * @param token_string the string read from a JSON that should be transformed
     *                     into a token.
     */
    public JSONToken(String token_string){
        switch (token_string){
            case START_OBJECT -> {
                this.type  = TOKEN_TYPE.START_OBJECT;
                this.value = START_OBJECT;
            }
            case END_OBJECT -> {
                this.type  = TOKEN_TYPE.END_OBJECT;
                this.value = END_OBJECT;
            }
            case START_ARRAY -> {
                this.type  = TOKEN_TYPE.START_ARRAY;
                this.value = START_ARRAY;
            }
            case END_ARRAY -> {
                this.type  = TOKEN_TYPE.END_ARRAY;
                this.value = END_ARRAY;
            }
            case ELEMENT_DELIMITER -> {
                this.type  = TOKEN_TYPE.ELEMENT_DELIMITER;
                this.value = ELEMENT_DELIMITER;
            }
            case KEY_VALUE_DELIMITER -> {
                this.type  = TOKEN_TYPE.KEY_VALUE_DELIMITER;
                this.value = KEY_VALUE_DELIMITER;
            }
            case "true", "false" -> {
                this.type  = TOKEN_TYPE.BOOLEAN;
                this.value = token_string.equals("true");
            }
            case "null" -> {
                this.type  = TOKEN_TYPE.NULL;
                this.value = null;
            }
            default -> {
                // Check if the value is enclosed in quotes
                if(token_string.startsWith("\"") && token_string.endsWith("\"")){
                    this.type = TOKEN_TYPE.STRING_LITERAL;
                    this.value = token_string.substring(1, token_string.length()-1);
                    return;
                }

                // Or if it is a number
                TOKEN_TYPE temp_type;
                Object temp_value;
                try{
                    temp_type  = TOKEN_TYPE.NUMBER;
                    temp_value = Double.parseDouble(token_string);
                }
                catch (NumberFormatException e){
                    // Otherwise token is invalid
                    temp_type  = TOKEN_TYPE.INVALID_TOKEN;
                    temp_value = null;
                }

                this.type  = temp_type;
                this.value = temp_value;

            }
        }
    }
}
