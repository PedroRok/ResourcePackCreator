package com.pedrorok.core.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.pedrorok.core.RPCore;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class JsonUtil {
    private static final Gson GSON = new GsonBuilder()
            .disableHtmlEscaping()
            .setPrettyPrinting()
            .create();

    private JsonUtil() {
    }

    public static JsonElement readFile(File file) {
        return readFile(file, null);
    }

    public static JsonElement readFile(File file, JsonElement defaultValue) {
        if (file == null) return defaultValue;
        Logger logger = RPCore.LOGGER;

        // If file does not exist.
        if (!file.exists()) {
            // Attempt to create the file.
            try {
                boolean create = file.createNewFile();
                if (create) logger.info("Created the {} file.", file.getName());
            } catch (IOException e) {
                logger.info("Could not create the {} file.", file.getName());
                e.printStackTrace();
            }
        }

        // Read the file.
        JsonElement element;
        try (FileReader reader = new FileReader(file)) {
            element = JsonParser.parseReader(reader);
        } catch (IOException e) {
            logger.info("Could not read the {} file.", file.getName());
            e.printStackTrace();
            return defaultValue;
        }

        return element;
    }

    public static void writeFile(@Nullable String mainFolder, String path, String fileName, JsonElement element) {

        StringBuilder builder = new StringBuilder("output/assets/");
        builder.append(mainFolder == null ? "minecraft" : mainFolder);
        builder.append("/models/");
        if (!path.endsWith("/")) path += "/";
        builder.append(path);
        if (!fileName.endsWith(".json")) fileName += ".json";
        builder.append(fileName);

        File file = WriterUtil.createFile(builder.toString());

        if (element == null) {
            boolean delete = file.delete();
            if (delete) RPCore.LOGGER.info("Created the {} file.", file.getName());
            return;
        }

        // Write the file.
        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write(GSON.toJson(element));
            fileWriter.flush();
        } catch (IOException e) {
            RPCore.LOGGER.info("Could not write the {} file.", file.getName(), e);
        }
    }
}