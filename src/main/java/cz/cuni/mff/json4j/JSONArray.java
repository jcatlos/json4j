package cz.cuni.mff.json4j;

import java.util.LinkedList;

/**
 * A wrapper for a JSON Array type.
 * Utilizes a Linked list for holding the values and provides
 * wrapper methods to operate on it.
 * Note that all added elements must be wrapped in the JSONValue wrapper.
 */
public class JSONArray {

    /**
     * The underlying LinkedList holding the values of the Array.
     */
    private LinkedList<JSONValue> values;

    /**
     * Get a value from the Array.
     * @param index Index of the element to retrieve.
     * @return The element at the requested index.
     */
    public JSONValue get(int index){
        return values.get(index);
    }

    /**
     * Add a value to the Array.
     * @param value Value to be added.
     */
    public void add(JSONValue value){
        values.add(value);
    }

    /**
     * Change the value at index of the Array.
     * @param index Index of the value to be changed.
     * @param value Value to be put at the index.
     */
    public void set(int index, JSONValue value){
        values.set(index, value);
    }

    /**
     * Get the size of the Array.
     * @return Number of elements in the Array.
     */
    public int size(){
        return values.size();
    }

    /**
     * Creates an empty JSON Array.
     */
    public JSONArray(){
        this.values = new LinkedList<>();
    }

    /**
     * Creates a JSON Array From the list of JSON Values.
     * @param values The LinkedList from which the JSONArray can be constructed.
     */
    public JSONArray(LinkedList<JSONValue> values){
        this.values = values;
    }

    /**
     * Serializes the Array in a space-saving manner.
     * Uses minimal possible number of characters required.
     * @return The compact serialization of the Array
     */
    public String serialize_compact(){
        StringBuilder out = new StringBuilder();
        out.append('[');
        for(JSONValue val: this.values){
            out.append(val.toString());
            out.append(',');
        }
        // Remove the last comma
        if(!this.values.isEmpty()) out.deleteCharAt(out.length()-1);
        out.append(']');

        return out.toString();
    }

    /**
     * Serializes the Array in a human-readable manner.
     * All values are printed on separate lines, indented. All inner multiline
     * values (Arrays and Objects) are indented as well.
     * Empty Array is printed only as a pair of brackets.
     * @return Human readable serialization of the Array.
     */
    public String serialize_readable(){
        StringBuilder out = new StringBuilder();
        out.append('[');
        if(!this.values.isEmpty()) out.append('\n');

        for(JSONValue val: this.values){
            out.append("    ");                         // Indent by 4 spaces
            String value_string = val.serialize_readable();
            // Indent all lines of multiline values (Arrays/Objects)
            if(value_string.lines().count() > 1){
                // Indent and remove indent from the first line
                value_string = value_string.indent(4).trim();
            }
            out.append(value_string);
            out.append(",\n");
        }

        // Remove last comma (but leave in the endline)
        if(!this.values.isEmpty()) out.deleteCharAt(out.length()-2);
        out.append(']');

        return out.toString();
    }

    /**
     * Uses the serialize_compact() method to output itself.
     * @return Compactly serialized JSONArray
     */
    @Override
    public String toString(){
        return this.serialize_compact();
    }
}
