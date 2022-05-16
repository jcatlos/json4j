package json4j;

/**
 * Represents a JSON type.
 * As of RFC-8259, a JSON value can be either a String, Number, JSON Object,
 * JSON Array, a Boolean ('true' / 'false') or 'null' (quotes are used to)
 * signify the precise token used for representation.
 */
enum JSON_TYPE {STRING, NUMBER, BOOLEAN, OBJECT, ARRAY, NULL}

/**
 * Represents a JSON value.
 * Holds the value itself and its type. The value itself is stored as Object.
 * Provides a constructor for each JSON type.
 */

public class JSONValue {

    public Object value;
    public JSON_TYPE type;

    public JSONValue(String value){
        this.value = value;
        this.type = JSON_TYPE.STRING;
    }

    public JSONValue(double value){
        this.value = value;
        this.type = JSON_TYPE.NUMBER;
    }

    public JSONValue(boolean value){
        this.value = value;
        this.type = JSON_TYPE.BOOLEAN;
    }

    public JSONValue(JSONObject value){
        this.value = value;
        this.type = JSON_TYPE.OBJECT;
    }

    public JSONValue(JSONList value){
        this.value = value;
        this.type = JSON_TYPE.ARRAY;
    }

    public JSONValue(){
        this.value = null;
        this.type = JSON_TYPE.NULL;
    }
}
