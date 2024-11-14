package com.pedrorok.core.modules.item;

import com.pedrorok.core.RPCore;
import com.pedrorok.core.utils.MinecraftItemAssets;
import com.pedrorok.core.utils.PredicateType;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Rok, Pedro Lucas nmm. Created on 12/11/2024
 * @project ResourcePackCreator
 */
public class RPItemModule {

    private final Map<String, ItemEntity> items = new HashMap<>();

    public RPItemModule() {
    }

    public void addItemCustomModel(String minecraftItemName, String path, int customModelData) {
        ItemEntity item = items.get(minecraftItemName);
        if (item == null) {
            MinecraftItemAssets byName = MinecraftItemAssets.getByName(minecraftItemName);
            if (byName == null) {
                throw new IllegalArgumentException("Minecraft item not found: " + minecraftItemName);
            }
            item = byName.toItemEntity();
            items.put(minecraftItemName, item);
        }
        item.predicateList().add(new Predicate<>(customModelData, PredicateType.CUSTOM_MODEL_DATA, path));
        //RPCore.LOGGER.info("Added custom model to item: {} with path: {} and predicateList: {}", minecraftItemName, path, customModelData);
        File file = new File("output/assets/minecraft/models/item/" + minecraftItemName + ".json");
        if (!file.exists()) {
            file.getParentFile().mkdirs();
        }

        item.writeFile(null);
    }

    public int addItemCustomModel(String minecraftItemName, String path) {

        int lastModelData = 0;
        if (items.containsKey(minecraftItemName)) {
            for (Predicate<?> predicate : items.get(minecraftItemName).predicateList()) {
                if (!predicate.type().equals(PredicateType.CUSTOM_MODEL_DATA)) continue;
                lastModelData = Math.max(lastModelData, (int) predicate.value());
            }
        }
        addItemCustomModel(minecraftItemName, path.replace(":", ":item/"), lastModelData + 1);
        return lastModelData + 1;
    }

    public void addItem(String path, ItemEntity item) {
        items.put(path, item);
        item.writeFile(path);
    }
}
