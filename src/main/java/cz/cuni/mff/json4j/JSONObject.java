package cz.cuni.mff.json4j;

import java.util.HashMap;
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
            out.append(String.format("\"%s\":%s,", pair.getKey(), pair.getValue()));
        }
        out.deleteCharAt(out.length()-1);
        out.append('}');

        return out.toString();
    }
}
