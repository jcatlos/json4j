package json4j;

import java.util.HashMap;

public class JSONObject {
    private HashMap<String, JSONValue> values;

    public void add(String key, JSONValue value){
        values.put(key, value);
    }

    public JSONValue get(String key){
        return values.get(key);
    }

    public JSONObject(){
        this.values = new HashMap<>();

    }
}
