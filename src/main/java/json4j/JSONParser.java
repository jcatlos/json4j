package json4j;

import java.util.ArrayList;
import java.util.Stack;

import org.apache.commons.lang.StringEscapeUtils;

public class JSONParser {

    int index;
    char current_char;
    String source;
    Stack<JSONElementBuilder> scope;

    // CHARACTER CONSTANTS
    static char COLON      = ':';
    static char COMMA      = ',';
    static char QUOTE      = '"';

    static char START_OBJECT   = '{';
    static char END_OBJECT     = '}';
    static char START_ARRAY    = '[';
    static char END_ARRAY      = ']';

    static char[] WHITESPACE = {' ', '\t', '\n', '\r'};


    public JSONParser(String source_string){

        // Handle all unicode character sequences in the source
        source = StringEscapeUtils.unescapeJava(source_string);

        scope = new Stack<>();

        // Initialize reading index and current_char at the beginning
        index = 0;
        if(source.length() > 0){
            current_char = source.charAt(0);
        }
    }

    private static boolean is_whitespace(char c){
        for(char ws: WHITESPACE){
            if(ws == c) return true;
        }
        return false;
    }

    private String get_next_token(){
        // TODo
        // Token = Zatvorka, dvojbodka, ciarka, uvodzovka, string
        return "";
    }

    private void evaluate_token(String token){
        //TODO
    }

    // Pridat streamove citanie?
    public ArrayList<JSONValue> parseString(String source_string) throws JSONMalformedSourceException {

        // The list of JSON values from the source
        ArrayList<JSONValue>  values = new ArrayList<>();
        JSONValue read_value;

        while(index < source.length()){
            String token = this.get_next_token();
            evaluate_token(token);
        }

        return values;
    }
}
