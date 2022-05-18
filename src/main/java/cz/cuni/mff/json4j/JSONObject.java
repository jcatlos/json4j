package cz.cuni.mff.json4j;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class JSONObject {
    private HashMap<String, JSONValue> values;

    // Pridat aj set funkciu
    public void add(String key, JSONValue value){
        values.put(key, value);
    }

    public void add(JSONKeyValuePair pair){
        values.put(pair.getKey(), pair.getValue());
    }

    public JSONValue get(String key){
        return values.get(key);
    }

    public JSONObject(){
        this.values = new HashMap<>();

    }

    @Override
    public String toString(){
        StringBuilder out = new StringBuilder();
        out.append('{');
        for (Entry<String, JSONValue> stringJSONValueEntry : this.values.entrySet()) {
            var pair = (Entry) stringJSONValueEntry;
            out.append("\"");
            out.append(pair.getKey());
            out.append("\"");
            out.append(':');
            out.append(pair.getValue().toString());
            out.append(',');
        }
        out.deleteCharAt(out.length()-1);
        out.append('}');

        return out.toString();
    }
}
