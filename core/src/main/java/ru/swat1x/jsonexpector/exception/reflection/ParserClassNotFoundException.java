package ru.swat1x.jsonexpector.exception.reflection;

public class ParserClassNotFoundException extends Exception {

    public ParserClassNotFoundException(String packageForLookup) {
        super("Can't find any parser for package " + packageForLookup);
    }
}
