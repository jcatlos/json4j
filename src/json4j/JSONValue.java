package json4j;

public class JSONValue {

    public enum JSON_TYPE {STRING, INTEGER, DECIMAL, OBJECT, LIST}

    public Object value;
    public JSON_TYPE type;

    public JSONValue(String value){
        this.value = value;
        this.type = JSON_TYPE.STRING;
    }

    public JSONValue(int value){
        this.value = value;
        this.type = JSON_TYPE.INTEGER;
    }

    public JSONValue(double value){
        this.value = value;
        this.type = JSON_TYPE.DECIMAL;
    }

    public JSONValue(JSONObject value){
        this.value = value;
        this.type = JSON_TYPE.OBJECT;
    }

    public JSONValue(JSONList value){
        this.value = value;
        this.type = JSON_TYPE.LIST;
    }
}
