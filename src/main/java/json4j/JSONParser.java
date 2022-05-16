package json4j;

import java.util.ArrayList;
import java.util.Stack;

import org.apache.commons.lang.StringEscapeUtils;

public class JSONParser {

    // CHARACTER CONSTANTS
    char COLON      = ':';
    char COMMA      = ',';
    char QUOTE      = '"';
    char ESCAPE     = '\\';

    char START_OBJECT   = '{';
    char END_OBJECT     = '}';
    char START_ARRAY    = '[';
    char END_ARRAY      = ']';

    char[] WHITESPACE = {' ', '\t', '\n', '\r'};

    private boolean is_whitespace(char c){
        for(char ws: WHITESPACE){
            if(ws == c) return true;
        }
        return false;
    }

    public static ArrayList<JSONValue> parseString(String source_string){

        // The list of JSON values from the source
        ArrayList<JSONValue>  values = new ArrayList<>();

        // Handle all unicode character sequences in the source
        String source = StringEscapeUtils.unescapeJava(source_string);

        // The top of the stack represents the current parsed JSON object or list
        Stack<JSONValue> scope = new Stack<>();


        return values;
    }
}
