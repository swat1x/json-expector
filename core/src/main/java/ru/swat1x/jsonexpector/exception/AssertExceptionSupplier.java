package ru.swat1x.jsonexpector.exception;

import ru.swat1x.jsonexpector.node.JsonNode;

public interface AssertExceptionSupplier {

    void throwFieldAbsent(JsonNode node, String fieldName);

    void throwFieldWrongType(JsonNode node, String fieldName, String expectedType, String actualValue);

    void throwFieldWrongValue(JsonNode node, String fieldName, String expectedValue, String actualValue);

    void throwObjectAbsent(JsonNode node, String fieldName);

    void throwArrayAbsent(JsonNode node, String fieldName);

}
