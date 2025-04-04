package ru.swat1x.jsonexpector.exception;

import ru.swat1x.jsonexpector.node.JsonNode;

public class BasicAssertExceptionSupplier implements AssertExceptionSupplier {

    @Override
    public void throwFieldAbsent(JsonNode node, String fieldName) {
        throw new JsonAssertionError("Field is absent in \"" + node.context().imaginedPath(fieldName) + "\"");
    }

    @Override
    public void throwFieldWrongType(JsonNode node, String fieldName, String expectedType, String actualValue) {
        throw new JsonAssertionError("Field is wrong type in \"" + fieldName + "\". Expected \"" + expectedType + "\" but it's value is \"" + actualValue+"\"");
    }

    @Override
    public void throwFieldWrongValue(JsonNode node, String fieldName, String expectedValue, String actualValue) {
        throw new JsonAssertionError("Field is wrong value in \"" + fieldName + "\". Expected " + expectedValue + " but actual is " + actualValue);
    }

    @Override
    public void throwObjectAbsent(JsonNode node, String fieldName) {
        throw new JsonAssertionError("Object is absent in \"" + fieldName+"\"");
    }

    @Override
    public void throwArrayAbsent(JsonNode node, String fieldName) {
        throw new JsonAssertionError("Array is absent in \"" + fieldName+"\"");
    }

}
