package ru.swat1x.jsonexpector.gson;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import ru.swat1x.jsonexpector.exception.AssertExceptionSupplier;
import ru.swat1x.jsonexpector.exception.basic.JsonParseException;
import ru.swat1x.jsonexpector.node.JsonTree;
import ru.swat1x.jsonexpector.parser.JsonParser;

@Getter
@Accessors(fluent = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class GsonJsonParser implements JsonParser {

    AssertExceptionSupplier exceptionSupplier;

    @Override
    public JsonTree parse(String json) throws JsonParseException {
        try {
            var gsonElement = (JsonObject) com.google.gson.JsonParser.parseString(json);
            var jsonTree = new GsonJsonTree(this, gsonElement);

            return jsonTree;
        } catch (JsonSyntaxException e) {
            throw new JsonParseException(e);
        }
    }

}
