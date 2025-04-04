package ru.swat1x.jsonexpector.node;

public interface JsonNodeContext {

    JsonNode parentNode();

    String formedPath();

    default String imaginedPath(String element) {
        return (formedPath().isEmpty() ? "" : formedPath() + ".") + element;
    }

}
