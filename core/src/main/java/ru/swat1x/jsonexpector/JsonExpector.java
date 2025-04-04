package ru.swat1x.jsonexpector;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import org.reflections.Reflections;
import ru.swat1x.jsonexpector.exception.AssertExceptionSupplier;
import ru.swat1x.jsonexpector.exception.BasicAssertExceptionSupplier;
import ru.swat1x.jsonexpector.exception.reflection.ParserClassNotFoundException;
import ru.swat1x.jsonexpector.exception.reflection.ParserInstanceCreationException;
import ru.swat1x.jsonexpector.parser.JsonParser;

import java.lang.reflect.Constructor;
import java.lang.reflect.InaccessibleObjectException;
import java.lang.reflect.InvocationTargetException;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JsonExpector {

    private static final String GSON_PARSER_CLASS = "ru.swat1x.jsonexpector.gson.GsonJsonParser";

    public static JsonExpector defaultExpector() {
        return new JsonExpector();
    }

    public static JsonExpectorConfiguration.Builder configurationBuilder() {
        return JsonExpectorConfiguration.builder();
    }

    public static JsonExpector withConfiguration(JsonExpectorConfiguration configuration) {
        return new JsonExpector(configuration);
    }

    AssertExceptionSupplier exceptionSupplier;

    private JsonExpector() {
        this(JsonExpectorConfiguration.builder().build());
    }

    private JsonExpector(JsonExpectorConfiguration configuration) {
        this.exceptionSupplier = configuration.exceptionSupplier();
    }

    public JsonParser createGsonParser() throws ParserInstanceCreationException {
        try {
            return createParser((Class<? extends JsonParser>) Class.forName(GSON_PARSER_CLASS));
        } catch (ClassNotFoundException e) {
            throw new ParserInstanceCreationException("Can't find default Gson parser. Check yours dependencies, maybe you forgot to import gson module");
        }
    }

    public JsonParser createParser(Class<? extends JsonParser> parserClass) throws ParserInstanceCreationException {
        var constructor = getParserConstructor(parserClass);
        var instance = createParserInstance(constructor);
        return instance;
    }

    private Constructor<? extends JsonParser> getParserConstructor(Class<? extends JsonParser> parserClass) throws ParserInstanceCreationException {
        try {
            var constructor = parserClass.getConstructor(
                    AssertExceptionSupplier.class
            );
            constructor.setAccessible(true);
            return constructor;
        } catch (NoSuchMethodException e) {
            throw new ParserInstanceCreationException("Invalid parser. Constructor must be like: ParserClass(AssertExceptionSupplier es)", e);
        } catch (InaccessibleObjectException e) {
            throw new ParserInstanceCreationException("Can't get access to constructor. Set it to public accessor", e);
        }
    }

    private JsonParser createParserInstance(Constructor<? extends JsonParser> constructor) throws ParserInstanceCreationException {
        try {
            return constructor.newInstance(
                    exceptionSupplier
            );
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new ParserInstanceCreationException("Can't create new instance of parser with constructor " + constructor, e);
        }
    }

    public JsonParser findParser(String basePackage) throws ParserClassNotFoundException, ParserInstanceCreationException {
        var reflections = new Reflections(basePackage);
        var parserClass = reflections.getSubTypesOf(JsonParser.class)
                .stream()
                .findFirst()
                .orElseThrow(() -> new ParserClassNotFoundException("Can't find any parser for package " + basePackage));
        var parserInstance = createParser(parserClass);
        return parserInstance;
    }

    public JsonParser findParser() throws ParserInstanceCreationException, ParserClassNotFoundException {
        return findParser("ru.swat1x.jsonexpector");
    }

    @Builder(builderClassName = "Builder")
    @Getter
    @Accessors(fluent = true)
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class JsonExpectorConfiguration {

        @lombok.Builder.Default
        AssertExceptionSupplier exceptionSupplier = new BasicAssertExceptionSupplier();

    }


}
