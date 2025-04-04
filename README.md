## JsonExpector

JsonExpector - is a Java library for flexible validation of JSON

> [!NOTE]\
> In close time I will add this library on maven repository

## Example usage:
Json in variable `EXAMPLE`
```json
{
  "str": "some string",
  "num": 1,
  "num2": 1000000000000000000,
  "num3": 42.52,
  "obj": {
    "some-field": "123"
  },
  "arr": [
    "element1",
    "element2"
  ]
}
```
Validation
```java
var parser = JsonExpector.defaultExpector()
                .createGsonParser();
var myTree = parser.parse(EXAMPLE);

myTree
    .expectString("str")
    .expectString("str", "some string")
    .expectNumber("num", 1)
    .expectNumber("num2", 1000000000000000000L)
    .expectNumber("num3", 42.52)
    .expectObject("obj")
    .ifObject("obj", obj -> {
        obj.expectString("some-field", "123");
    })
    .expectArray("arr");
```

## Customization
> [!IMPORTANT]\
> If you want to add your custom parser it's need to have constructor with only one parameter (AssertExceptionSupplier)

Customization of parser behaviour
```java
 var config = JsonExpector.configurationBuilder()
                .exceptionSupplier(new MyCustomExpectionSupplier())
                .build();
var jsonExpector = JsonExpector.withConfiguration(config);
var gsonParser = jsonExpector.createGsonParser();
var gsonClassParser = jsonExpector.createParser(GsonJsonParser.class);
var customParser = jsonExpector.createParser(MyCustomParser.class);
```

```java
@Getter
@Accessors(fluent = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class MyCustomParser implements JsonParser {

    // IMPORTANT!! READ NOTE!!
    AssertExceptionSupplier exceptionSupplier;

    @Override
    public JsonTree parse(String json) throws JsonParseException {
        try {
            var parserElement = SomeJsonParser.parseString(json); // any third-party parser library
            var jsonTree = new CustomJsonTree(this, parserElement);

            return jsonTree;
        } catch (JsonSyntaxException e) {
            throw new JsonParseException(e);
        }
    }

}
```

