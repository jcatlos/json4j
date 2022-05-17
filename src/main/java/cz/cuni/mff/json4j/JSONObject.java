package cz.cuni.mff.json4j;

import java.util.HashMap;

public class JSONObject {
    private HashMap<String, JSONValue> values;

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
}
