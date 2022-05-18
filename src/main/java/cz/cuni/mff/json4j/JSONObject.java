package cz.cuni.mff.json4j;

import java.util.HashMap;
import java.util.Map.Entry;

public class JSONObject {
    private HashMap<String, JSONValue> values;

    public void put(String key, JSONValue value){
        values.put(key, value);
    }

    public JSONValue get(String key){
        return values.get(key);
    }

    public JSONObject(){
        this.values = new HashMap<>();
    }

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

    @Override
    public String toString(){
        return this.serialize_compact();
    }
}
