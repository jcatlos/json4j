package cz.cuni.mff.json4j;


/**
 * An exception when the parsed JSON is breaking any rules.
 * The rules are specified in RFC-8252.
 * Notable errors are: missing colons and commas, unfinished objects and arrays,
 * non-string keys of objects or invalid literals.
 * A message is provided to describe the found error.
 * Upon throwing this exception, the parsing of the string is stopped.
 */
public class JSONMalformedSourceException extends Exception{
    public JSONMalformedSourceException(String message){
        super(message);
    }
}
