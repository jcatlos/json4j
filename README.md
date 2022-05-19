# json4j
A java library for workling with JSON files. Provides an 
easy interface to parse, modify and generate JSON files.
The implementation details are based on the **RFC-8259**
that codifies JSON.

In this user documentation, we first discuss the implemented interface
for interacting with JSON and then about ways to serialize and deserialize 
JSON.

## Type representation
This section talks about the usage of the classes implemented for 
working with JSON.

### JSON Value
The basic building block of the library is a JSONValue. It holds all values
supported by JSON - that means JSON Array and Objects, String, number (double), 
boolean or null. The constructor is overloaded for all compatible types.
For JSON Arrays and Objects, there are implemented wrappers described below.

All JSONValues provide functionality to be serialized.

### JSONArray
This is a wrapper type for the JSON Array. It is implemented by a 
`LinkedList<JSONValue>` and provides an interface to get, add, set 
and remove. Furthermore, the size of the JSONArray can be queried. 

It can be constructed empty or based off of a `LinkedList<SONValue>`.

### JSONObject
This is a wrapper type for the JSON Object. It is implemented by a 
`HashMap<String, JSONValue>`, and provides an interface to get, put
and remove.

> Please note that the key is of type `String` and not a `JSONValue`.
> Therefore all functions requiring a key take a `String` as a parameter.
> This is because JSON accepts only strings as keys in objects.

It can be constructed empty or based off of a `HashMap<String, JSONValue>`.

### Example snippet
In the example below we create a JSON Object containing numbers from 
one to four and for each one of them the information about their parity.
```java
JSONObject odd_obj = new JSONObject();
    for(int i=0; i<5; i++){
    JSONValue wrapped = new JSONValue(i%2==0);
    odd_obj.put(String.valueOf(i), wrapped);
}
    
System.out.println(odd_obj.serialize_readable());
```
Should output
```
{
    0 : true,
    1 : false,
    2 : true,
    3 : false,
    4 : true
}
```

## Serialization/Deserialization
This section describes means to serialize and deserialize 
JSON using this library.

### JSONParser
For deserializing JSON from strings we implemented the JSONParser class.
It is constructed with a `String` as a parameter. It expects this string to
contain a single JSON value (as is often the norm). Then, by calling the
`parseString()` method, it parses the input string and returns a single
`JSONValue` in the file. During the parsing, it also checks the form of
the JSON and fails if it finds any mistakes.

### Serializing JSONValues
For the serialization, the `JSONValue` class has implemented methods
`serialize_compact()` and `serialize_readable()`. Both return the value
in a JSON serialized format, but each takes a different approach.

The `serialize_compact()` serializes the JSON without using any
whitespace. This is usable in scenarios of transporting of larger files,
where the saved characters could make an impact.

The `serialiize_readable()` serializes the JSON in a readable way.
The arrays and objects are unwrapped so that every element takes one 
line, and the object hierarchy is pictured by increasing indentation.

Note, that these 2 functions show a difference only when used on 
Arrays and objects, as all other literals are printed the same.

```
Example of serialize_compact():
{"name":"John Doe","address":{"street_name":"Patkova 3","postcode":18000,"city":"Praha"}}

Example of serialize_readable():
{
    "name" : "John Doe",
    "address" : {
        "street_name" : "Patkova 3",
        "postcode" : 18000,
        "city" : "Praha"
        }
}
```