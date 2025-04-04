package ru.swat1x.jsonexpector.node;

import ru.swat1x.jsonexpector.parser.JsonParser;

import java.util.function.Consumer;

public interface JsonNode {

    String stringValue(String fieldName);

    // For fields

    JsonNode expectField(String fieldName);

    // String Field

    JsonNode expectString(String fieldName);

    JsonNode expectString(String fieldName, String value);

    JsonNode expectStringIgnoreCase(String fieldName, String value);

    // Number Field

    JsonNode expectNumber(String fieldName);

    JsonNode expectNumber(String fieldName, Number expectedNumber);

    // Objects

    JsonNode expectObject(String sectionFieldName);

    JsonNode ifObject(String sectionFieldName, Consumer<JsonNode> objectConsumer);

    // Arrays

    JsonNode expectArray(String arrayFieldName);

    JsonNode ifArray(String arrayFieldName, Consumer<JsonNode> arrayConsumer);


    <T> T toObject(Class<T> fromJsonClass);

    JsonTree rootTree();

    JsonNodeContext context();

}
