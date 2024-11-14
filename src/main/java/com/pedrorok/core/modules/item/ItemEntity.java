package com.pedrorok.core.modules.item;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.pedrorok.core.utils.JsonUtil;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Rok, Pedro Lucas nmm. Created on 12/11/2024
 * @project ResourcePackCreator
 */
public record ItemEntity(String name, String parent, Map<String, String> textures, List<Predicate<?>> predicateList) {

    public ItemEntity(String name, String parent) {
        this(name, parent, new HashMap<>(), new ArrayList<>());
    }

    public void writeFile(@Nullable String path) {
        // Cria um elemento json e escreve as propriedades nele como objeto
        JsonObject jsonElement = new JsonObject();
        jsonElement.add("parent", new JsonPrimitive(parent));
        JsonObject jsonTextures = new JsonObject();
        for (Map.Entry<String, String> entry : textures.entrySet()) {
            jsonTextures.add(entry.getKey(), new JsonPrimitive(entry.getValue()));
        }
        jsonElement.add("textures", jsonTextures);

        JsonArray overrides = new JsonArray();

        for (Predicate<?> predicateValue : predicateList) {
            JsonObject override = new JsonObject();
            JsonObject predicate = new JsonObject();
            predicate.add(predicateValue.type().getValue(), new JsonPrimitive(predicateValue.value()));
            override.add("predicate", predicate);
            override.add("model", new JsonPrimitive(predicateValue.modelPath()));
            overrides.add(override);
        }

        jsonElement.add("overrides", overrides);

        String mainFolder = null;
        String pathFile = "item";
        if (path != null) {
            String[] pathSplit = path.split(":");
            mainFolder = pathSplit[0];
            pathFile = pathSplit[1];
        }

        JsonUtil.writeFile(mainFolder, pathFile, name, jsonElement);
    }
}
