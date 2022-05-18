package cz.cuni.mff.json4j;

import java.util.LinkedList;

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

    @Override
    public String toString(){
        StringBuilder out = new StringBuilder();
        out.append('[');
        for(JSONValue val: this.values){
            out.append(val.toString());
            out.append(',');
        }
        out.deleteCharAt(out.length()-1);
        out.append(']');

        return out.toString();
    }
}
