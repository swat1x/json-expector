package ru.swat1x.jsonexpector.gson;

import com.google.gson.JsonObject;
import ru.swat1x.jsonexpector.node.BasicNodeContext;
import ru.swat1x.jsonexpector.node.JsonTree;

public class GsonJsonTree extends GsonJsonNode implements JsonTree {

    protected GsonJsonTree(GsonJsonParser parser,
                        JsonObject gsonElement) {
        super(null,
                new BasicNodeContext(
                        parser,
                        null,
                        ""
                ), gsonElement);
    }

}
