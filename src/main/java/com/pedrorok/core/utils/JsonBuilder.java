package com.pedrorok.core.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

/**
 * @author Rok, Pedro Lucas nmm. Created on 15/11/2024
 * @project ResourcePackCreator
 */
public class JsonBuilder {

    private JsonObject mainObject = new JsonObject();
    private JsonObject textures = new JsonObject();
    private JsonArray overrides = new JsonArray();

    public JsonBuilder() {}

    public JsonBuilder(String parent) {
        mainObject.add("parent", new JsonPrimitive(parent));
    }

    public JsonBuilder(JsonObject build) {
        if (build.has("textures")) {
            textures = build.getAsJsonObject("textures");
        }
        if (build.has("overrides")) {
            overrides = build.getAsJsonArray("overrides");
        }
        this.mainObject = build;
    }

    public JsonBuilder addTexture(String value) {
        int layerNumber = 0;
        while (textures.has("layer" + layerNumber)) {
            layerNumber++;
        }
        textures.add("layer" + layerNumber, new JsonPrimitive(value));
        return this;
    }

    public JsonBuilder addTexture(String key, String value) {
        textures.add(key, new JsonPrimitive(value));
        return this;
    }

    public JsonBuilder setParent(String value) {
        mainObject.add("parent", new JsonPrimitive(value));
        return this;
    }

    public JsonBuilder addOverride(String key, Number value, String model) {
        JsonObject override = new JsonObject();
        JsonObject predicate = new JsonObject();
        predicate.add(key, new JsonPrimitive(value));
        override.add("predicate", predicate);
        override.add("model", new JsonPrimitive(model));
        overrides.add(override);
        return this;
    }

    public JsonBuilder addCustomModel(int value, String model) {
        JsonObject override = new JsonObject();
        JsonObject predicate = new JsonObject();
        predicate.add("custom_model_data", new JsonPrimitive(value));
        override.add("predicate", predicate);
        override.add("model", new JsonPrimitive(model));
        overrides.add(override);
        return this;
    }

    public JsonObject build() {
        if (!textures.isEmpty())
            mainObject.add("textures", textures);
        if (!overrides.isEmpty())
            mainObject.add("overrides", overrides);
        return mainObject;
    }
}
