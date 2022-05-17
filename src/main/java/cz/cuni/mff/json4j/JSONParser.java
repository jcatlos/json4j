package cz.cuni.mff.json4j;

import java.util.ArrayList;
import java.util.Stack;

import org.apache.commons.lang.StringEscapeUtils;

public class JSONParser {

    int index;
    char current_char;
    String source;
    Stack<JSONElementBuilder> scope;

    // CHARACTER CONSTANTS
    final static char QUOTE  = '"';
    final static char[] WHITESPACE = {' ', '\t', '\n', '\r'};
    final static char[] TOKEN_CHAR = {'{', '}', '[', ']', ',', ':'};

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

    private void increment_index(){
        index++;
        current_char = source.charAt(index);
    }

    private static boolean is_whitespace(char c){
        for(char ws: WHITESPACE){
            if(ws == c) return true;
        }
        return false;
    }

    private static boolean is_token_char(char c){
        for(char tk: TOKEN_CHAR){
            if(tk == c) return true;
        }
        return false;
    }

    private JSONToken get_next_token(){
        // Read all whitespace before token
        while(is_whitespace(current_char)){
            this.increment_index();
        }

        StringBuilder token_sb = new StringBuilder();

        if(current_char == QUOTE){
           do {
                token_sb.append(current_char);
                this.increment_index();
            }  while(current_char != QUOTE);
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
            }  while(!is_whitespace(current_char) && !is_token_char(current_char));
        }

        return new JSONToken(token_sb.toString());
    }

    private void evaluate_token(JSONToken token){
        //TODO
    }

    // Pridat streamove citanie?
    public ArrayList<JSONValue> parseString() throws JSONMalformedSourceException {

        // The list of JSON values from the source
        ArrayList<JSONValue>  values = new ArrayList<>();
        JSONValue read_value;

        while(index < source.length()){
            JSONToken token = this.get_next_token();
            System.out.println(token.type.toString());
            //evaluate_token(token);
        }

        return values;
    }
}
