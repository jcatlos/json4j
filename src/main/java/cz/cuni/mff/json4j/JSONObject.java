package cz.cuni.mff.json4j;

import java.util.HashMap;
import java.util.Map.Entry;

/**
 * A wrapper for a JSON Object type.
 * Utilizes a HashMap for holding the keys and values and provides
 * wrapper methods to operate on it.
 * Note that all added keys must pe plain Strings and all values
 * must be wrapped in the JSONValue wrapper.
 */
public class JSONObject {

    /**
     * The underlying hashMap holding the values of the Object.
     */
    private HashMap<String, JSONValue> values;

    /**
     * Associate a value to the key
     * @param key String to be used as a key. Note that this MUST NOT be
     *            wrapped in the JSOValue wrapper.
     * @param value The value to be associated to the key. This, on the
     *              other hand, MUST be wrapped in the JSONValue wrapper
     */
    public void put(String key, JSONValue value){
        values.put(key, value);
    }

    /**
     * Removes the value associated with the key.
     * @param key of the value to be removed.
     * @return The removed value.
     */
    public JSONValue remove(String key){
        return values.remove(key);
    }

    /**
     * Get the value associated to the provided key.
     * @param key Key used to find the value. Note that this MUST NOT be
     *            wrapped in the JSOValue wrapper.
     * @return JSON Value associated to the key.
     */
    public JSONValue get(String key){
        return values.get(key);
    }

    /**
     * Creates an empty JSON Object.
     */
    public JSONObject(){
        this.values = new HashMap<>();
    }

    /**
     * Creates a JSON Object from a HashMap.
     * @param values The HashMap from which the JSONObject can be constructed.
     */
    public JSONObject(HashMap<String, JSONValue> values){
        this.values = values;
    }

    /**
     * Serializes the Object in a human-readable manner.
     * All key-value pairs are printed on separate lines, indented. All inner
     * multiline values (Arrays and Objects) are indented as well.
     * Empty Object is printed only as a pair of brackets.
     * @return Human readable serialization of the Object.
     */
    public String serialize_readable(){
        StringBuilder out = new StringBuilder();
        out.append('{');
        if(!this.values.isEmpty()) out.append('\n');

        for (Entry<String, JSONValue> setEntry : this.values.entrySet()) {
            var pair = (Entry<String, JSONValue>) setEntry;
            String key = pair.getKey();
            JSONValue value = pair.getValue();

            out.append("    ");                         // Indent by 4 spaces
            String value_string = value.serialize_readable();
            // Indent all lines of multiline values (Arrays/Objects)
            if(value_string.lines().count() > 1){
                // Indent and remove indent from the first line
                value_string = value_string.indent(4).trim();
            }
            out.append(String.format("%s : %s,\n", key, value_string));
        }

        // Remove last comma (but leave in the endline)
        if(!this.values.isEmpty()) out.deleteCharAt(out.length()-2);
        out.append('}');

        return out.toString();
    }

    /**
     * Serializes the Object in a space-saving manner.
     * Uses minimal possible number of characters required.
     * @return The compact serialization of the Object
     */
    public String serialize_compact(){
        StringBuilder out = new StringBuilder();
        out.append('{');
        for (Entry<String, JSONValue> stringJSONValueEntry : this.values.entrySet()) {
            var pair = (Entry<String, JSONValue>) stringJSONValueEntry;
            out.append(String.format("\"%s\":%s,", pair.getKey(), pair.getValue()));
        }
        out.deleteCharAt(out.length()-1);
        out.append('}');

        return out.toString();
    }

    /**
     * Uses the serialize_compact() method to output itself.
     * @return Compactly serialized JSONObject
     */
    @Override
    public String toString(){
        return this.serialize_compact();
    }
}
