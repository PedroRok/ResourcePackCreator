package com.pedrorok.core.modules.item;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.pedrorok.core.utils.JsonBuilder;
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

        JsonBuilder jsonBuilder = new JsonBuilder(parent);
        for (Map.Entry<String, String> entry : textures.entrySet()) {
            jsonBuilder.addTexture(entry.getKey(), entry.getValue());
        }

        for (Predicate<?> predicateValue : predicateList) {
            jsonBuilder.addOverride(predicateValue.type().getValue(), predicateValue.value(), predicateValue.modelPath());
        }

        String mainFolder = null;
        String pathFile = "item";
        if (path != null) {
            String[] pathSplit = path.split(":");
            mainFolder = pathSplit[0];
            pathFile = pathSplit[1];
        }

        JsonUtil.writeFile(mainFolder, pathFile, name, jsonBuilder.build());
    }
}
