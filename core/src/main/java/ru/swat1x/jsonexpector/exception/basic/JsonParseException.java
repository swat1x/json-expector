package ru.swat1x.jsonexpector.exception.basic;

public class JsonParseException extends RuntimeException {

    public JsonParseException(Throwable cause) {
        super("Can't parse json", cause);
    }
}
