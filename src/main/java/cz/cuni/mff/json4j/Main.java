package cz.cuni.mff.json4j;

public class Main {

    public static void main(String[] args) throws JSONMalformedSourceException, JSONUnfinishedStringAtEOF {
        String test_json = "{\"age:\" : 22}";
        JSONParser parser = new JSONParser(test_json);

        parser.parseString();
    }
}
