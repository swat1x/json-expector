package ru.swat1x.jsonexpector.node;

public interface JsonTree extends JsonNode {

    @Override
    default JsonTree rootTree() {
        return this;
    }

}
