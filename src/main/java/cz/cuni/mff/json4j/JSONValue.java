package cz.cuni.mff.json4j;

import java.util.Objects;

/**
 * A wrapper for a value to be used by the library.
 * All JSON supported types are supported. Note that Objects and Arrays have
 * their additional wrapper class.
 * Holds the value itself and its type. The value itself is stored as Object.
 * Provides a constructor for each JSON type. The provided type is then used
 * to infer the this.type field.
 */

public class JSONValue {

    /**
     * The actual value of the JSONValue.
     * Final to prevent machinations with changing the value and mismatching it with
     * the type or vice versa.
     */
    public final Object value;

    /**
     * The type of the value held.
     */
    public final JSON_TYPE type;

    /**
     * The constructor for a string value.
     * @param value String to be wrapped.
     */
    public JSONValue(String value){
        this.value = value;
        this.type = JSON_TYPE.STRING;
    }

    /**
     * The constructor for a numeric value.
     * @param value Number (double) to be wrapped.
     */
    public JSONValue(double value){
        this.value = value;
        this.type = JSON_TYPE.NUMBER;
    }

    /**
     * The constructor for a boolean value.
     * @param value Boolean to be wrapped.
     */
    public JSONValue(boolean value){
        this.value = value;
        this.type = JSON_TYPE.BOOLEAN;
    }

    /**
     * The constructor for a JSON Object.
     * The value is passed using the JSONObject wrapper class.
     * @param value JSON Object to be wrapped.
     */
    public JSONValue(JSONObject value){
        this.value = value;
        this.type = JSON_TYPE.OBJECT;
    }

    /**
     * The constructor for a JSON Array.
     * The value is passed using the JSONArray wrapper class.
     * @param value JSON Array to be wrapped.
     */
    public JSONValue(JSONArray value){
        this.value = value;
        this.type = JSON_TYPE.ARRAY;
    }

    /**
     * The constructor for a null value.
     * Since there is no sensible type for null, we decided to use the
     * default constructor to produce null values.
     */
    public JSONValue(){
        this.value = null;
        this.type = JSON_TYPE.NULL;
    }

    /**
     * Serializes the value in a space-saving variant.
     * All whitespaces are omitted. This reduces the occupied space but does
     * not affect its machine-readability.
     * This makes a difference only for JSON Objects and arrays, otherwise the
     * default Object.toString() is used. Note that for String, the quote-chars are
     * added to the output.
     * @return compactly serialized String representation of the JSON Value
     */
    public String serialize_compact(){
        String printValue = this.value != null ? this.value.toString() : "";
        if(this.type == JSON_TYPE.STRING){
            return "\"" + printValue+ "\"";
        }
        return printValue;
    }

    /**
     * Serializes the JSON Value in a more readable manner.
     * Changes its behavior compared to serialize_compact() only for JSON Objects and Arrays.
     * Prints them out in an indented form.
     * @return Formatted String representation of the JSONValue
     */
    public String serialize_readable(){
        if(this.type == JSON_TYPE.ARRAY){
            JSONArray arr = (JSONArray) this.value;
            return Objects.requireNonNull(arr).serialize_readable();
        }
        if(this.type == JSON_TYPE.OBJECT){
            JSONObject obj = (JSONObject) this.value;
            return Objects.requireNonNull(obj).serialize_readable();
        }
        return this.serialize_compact();
    }

    /**
     * Uses the serialize_compact() method to output itself.
     * @return Compactly serialized JSONValue
     */
    @Override
    public String toString(){
        return this.serialize_compact();
    }
}
