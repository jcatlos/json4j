package cz.cuni.mff.json4j;

import java.util.LinkedList;

public class JSONList {
    private LinkedList<JSONValue> values;
    private int length;

    public JSONValue get(int index){
        return values.get(index);
    }

    public void add(JSONValue value){
        values.add(value);
    }

    public JSONList(){
        this.values = new LinkedList<>();
        this.length = 0;
    }
}
