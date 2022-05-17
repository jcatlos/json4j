package cz.cuni.mff.json4j;

enum TOKEN_TYPE {
    START_OBJECT,
    END_OBJECT,
    START_ARRAY,
    END_ARRAY,
    ELEMENT_DELIMITER,
    KEY_VALUE_DELIMITER,
    STRING_LITERAL,
    NUMBER,
    BOOLEAN,
    NULL,
    INVALID_TOKEN,
    EOF
    ;

    public boolean isValue(){
        return(
                this == STRING_LITERAL ||
                        this == NUMBER ||
                        this == BOOLEAN ||
                        this == NULL ||
                        this == START_OBJECT ||
                        this == START_ARRAY
        );
    }
}

public class JSONToken {

    // CHARACTER CONSTANTS
    final static String KEY_VALUE_DELIMITER = ":";
    final static String ELEMENT_DELIMITER   = ",";

    final static String START_OBJECT  = "{";
    final static String END_OBJECT    = "}";
    final static String START_ARRAY   = "[";
    final static String END_ARRAY     = "]";

    public final TOKEN_TYPE type;
    public final Object value;


    public static JSONToken generateEOFToken(){
        return new JSONToken(TOKEN_TYPE.EOF, null);
    }

    private JSONToken(TOKEN_TYPE type, Object value){
        this.type = type;
        this.value = value;
    }

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
                    temp_value = Integer.parseInt(token_string);
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