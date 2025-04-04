package ru.swat1x.jsonexpector.node;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import ru.swat1x.jsonexpector.parser.JsonParser;

@Getter
@Accessors(fluent = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class BasicNodeContext implements JsonNodeContext {

    JsonParser parser;
    JsonNode parentNode;
    String formedPath;

}
