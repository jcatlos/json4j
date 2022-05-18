package cz.cuni.mff.json4j;

import java.util.LinkedList;

/**
 * A wrapper for a JSON Array type.
 * Utilizes a Linked list for holding the values and provides
 * wrapper methods to operate on it.
 */
public class JSONArray {
    private LinkedList<JSONValue> values;

    public JSONValue get(int index){
        return values.get(index);
    }

    public void add(JSONValue value){
        values.add(value);
    }

    public void set(int index, JSONValue value){
        values.set(index, value);
    }

    public int size(){
        return values.size();
    }

    public JSONArray(){
        this.values = new LinkedList<>();
    }

    public JSONArray(LinkedList<JSONValue> values){
        this.values = values;
    }

    public String serialize_compact(){
        StringBuilder out = new StringBuilder();
        out.append('[');
        for(JSONValue val: this.values){
            out.append(val.toString());
            out.append(',');
        }
        if(!this.values.isEmpty()) out.deleteCharAt(out.length()-1);
        out.append(']');

        return out.toString();
    }

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

    @Override
    public String toString(){
        return this.serialize_compact();
    }
}
