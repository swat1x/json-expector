package ru.swat1x.jsonexpector.gson;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import ru.swat1x.jsonexpector.node.BasicNodeContext;
import ru.swat1x.jsonexpector.node.JsonNode;
import ru.swat1x.jsonexpector.node.JsonTree;
import ru.swat1x.jsonexpector.util.NumberComparatorUtil;

import java.lang.reflect.Type;
import java.util.function.Consumer;

@Getter
@Accessors(fluent = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class GsonJsonNode implements JsonNode {

    JsonTree rootTree;
    BasicNodeContext context;

    com.google.gson.JsonObject gsonElement;

    private GsonJsonNode jumpInto(String elementName) {
        var element = this.gsonElement.getAsJsonObject(elementName);
        return new GsonJsonNode(
                rootTree,
                new BasicNodeContext(
                        context.parser(),
                        this,
                        context.imaginedPath(elementName)
                ),
                element
        );
    }

    public JsonElement validatedElement(String elementName) {
        var element = this.gsonElement.get(elementName);
        if (element == null || element.isJsonNull()) {
            this.context.parser().exceptionSupplier()
                    .throwFieldAbsent(this, elementName);
        }
        return element;
    }

    public JsonPrimitive validatedPrimitive(String elementName) {
        try {
            return this.gsonElement.getAsJsonPrimitive(elementName);
        } catch (ClassCastException e) {
            this.context.parser().exceptionSupplier()
                    .throwFieldWrongType(this, elementName, "primitive type", "undefined");
            return null; // Unreached with right realization of exception supplier
        }
    }

    @Override
    public String stringValue(String fieldName) {
        this.expectField(fieldName);

        return this.gsonElement.get(fieldName).getAsString();
    }

    @Override
    public JsonNode expectField(String fieldName) {
        if (!this.gsonElement.has(fieldName)) {
            this.context.parser().exceptionSupplier()
                    .throwFieldAbsent(this, fieldName);
        }
        return this;
    }

    @Override
    public JsonNode expectString(String fieldName) {
        var fieldElement = this.validatedPrimitive(fieldName);
        if (!fieldElement.isString()) {
            this.context.parser().exceptionSupplier()
                    .throwFieldWrongType(this, fieldName, "string", fieldElement.getAsString());
        }

        return this;
    }

    @Override
    public JsonNode expectString(String fieldName, String value) {
        return this.expectString(fieldName, value, false);
    }

    @Override
    public JsonNode expectStringIgnoreCase(String fieldName, String value) {
        return this.expectString(fieldName, value, true);
    }

    private JsonNode expectString(String fieldName, String value, boolean ignoreCase) {
        this.expectString(fieldName);

        var fieldStringValue = this.validatedPrimitive(fieldName).getAsString();
        if (!(ignoreCase ?
                fieldStringValue.equalsIgnoreCase(value) :
                fieldStringValue.equals(value))) {
            this.context.parser().exceptionSupplier()
                    .throwFieldWrongValue(this, fieldName, value, fieldStringValue);
        }

        return this;
    }

    @Override
    public JsonNode expectNumber(String fieldName) {
        var fieldElement = this.validatedPrimitive(fieldName);
        if (!fieldElement.isNumber()) {
            this.context.parser().exceptionSupplier()
                    .throwFieldWrongType(this, fieldName, "number", fieldElement.getAsString());
        }

        return this;
    }

    @Override
    public JsonNode expectNumber(String fieldName, Number expectedNumber) {
        this.expectNumber(fieldName);

        var fieldNumberValue = this.validatedElement(fieldName).getAsNumber();
        if (NumberComparatorUtil.compareNumbers(fieldNumberValue, expectedNumber)) {
            this.context.parser().exceptionSupplier()
                    .throwFieldWrongValue(this, fieldName, String.valueOf(expectedNumber), String.valueOf(fieldNumberValue));
        }

        return this;
    }

    @Override
    public JsonNode expectObject(String sectionFieldName) {
        var element = this.validatedElement(sectionFieldName);
        if (!element.isJsonObject()) {
            this.context.parser().exceptionSupplier()
                    .throwObjectAbsent(this, sectionFieldName);
        }
        return this;
    }

    @Override
    public JsonNode ifObject(String sectionFieldName, Consumer<JsonNode> objectConsumer) {
        this.expectObject(sectionFieldName);
        var childNode = this.jumpInto(sectionFieldName);
        objectConsumer.accept(childNode);
        return this;
    }

    @Override
    public JsonNode expectArray(String arrayFieldName) {
        var element = this.validatedElement(arrayFieldName);
        if (!element.isJsonArray()) {
            this.context.parser().exceptionSupplier()
                    .throwArrayAbsent(this, arrayFieldName);
        }
        return this;
    }

    @Override
    public JsonNode ifArray(String arrayFieldName, Consumer<JsonNode> arrayConsumer) {
        this.expectArray(arrayFieldName);
        var childNode = this.jumpInto(arrayFieldName);
        arrayConsumer.accept(childNode);
        return this;
    }

    @Override
    public <T> T toObject(Class<T> fromJsonClass) {
        return new Gson().fromJson(gsonElement, fromJsonClass);
    }

    // Gson abilities

    public Object toObject(Type fromJsonClass) {
        return new Gson().fromJson(gsonElement, fromJsonClass);
    }

}
