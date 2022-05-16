package json4j;

public class JSONKeyValuePair {
    private String key;
    private JSONValue value;

    public String getKey(){
        return this.key;
    }

    public JSONValue getValue(){
        return this.value;
    }

    public JSONKeyValuePair(String key, JSONValue value){
        this.key = key;
        this.value = value;
    }
}
