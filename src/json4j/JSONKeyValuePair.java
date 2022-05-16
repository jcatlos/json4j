package json4j;

public class JSONKeyValuePair {
    private String key;
    private String value;

    public String getKey(){
        return this.key;
    }

    public String getValue(){
        return this.value;
    }

    public JSONKeyValuePair(String key, String value){
        this.key = key;
        this.value = value;
    }
}
