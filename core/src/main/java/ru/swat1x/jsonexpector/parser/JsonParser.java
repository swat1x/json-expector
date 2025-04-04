package ru.swat1x.jsonexpector.parser;

import ru.swat1x.jsonexpector.exception.AssertExceptionSupplier;
import ru.swat1x.jsonexpector.exception.basic.JsonParseException;
import ru.swat1x.jsonexpector.node.JsonNode;
import ru.swat1x.jsonexpector.node.JsonTree;

public interface JsonParser {

    AssertExceptionSupplier exceptionSupplier();

    JsonTree parse(String json) throws JsonParseException;

}
