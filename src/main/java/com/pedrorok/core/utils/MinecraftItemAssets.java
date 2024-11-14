package com.pedrorok.core.utils;

import com.pedrorok.core.modules.item.ItemEntity;
import com.pedrorok.core.modules.item.Predicate;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Rok, Pedro Lucas nmm. Created on 12/11/2024
 * @project ResourcePackCreator
 */
@Getter
public enum MinecraftItemAssets {

    LEATHER_HELMET("leather_helmet", "minecraft:item/generated", getTrims("leather_helmet"),"minecraft:item/leather_helmet", "minecraft:item/leather_helmet_overlay"),
    LEATHER_CHESTPLATE("leather_chestplate", "minecraft:item/generated", getTrims("leather_chestplate"),"minecraft:item/leather_chestplate", "minecraft:item/leather_chestplate_overlay"),
    LEATHER_LEGGINGS("leather_leggings", "minecraft:item/generated", getTrims("leather_leggings"),"minecraft:item/leather_leggings", "minecraft:item/leather_leggings_overlay"),
    LEATHER_BOOTS("leather_boots", "minecraft:item/generated", getTrims("leather_boots"),"minecraft:item/leather_boots", "minecraft:item/leather_boots_overlay"),
    PAPER("paper", "minecraft:item/generated", null,"minecraft:item/paper");


    private final String fileName;
    private final String parent;
    private final List<Predicate<?>> predicates;
    private final String[] textures;

    <T extends Number> MinecraftItemAssets(String fileName, String parent, List<Predicate<?>> predicates, String... textures) {
        this.fileName = fileName;
        this.parent = parent;
        this.predicates = predicates;
        this.textures = textures;
    }

    public ItemEntity toItemEntity() {
        ItemEntity item = new ItemEntity(fileName, parent);
        for (int i = 0; i < textures.length; i++) {
            item.textures().put("layer"+i, textures[i]);
        }
        if (predicates != null)
            item.predicateList().addAll(predicates);

        return item;
    }

    public static MinecraftItemAssets getByName(String name) {
        for (MinecraftItemAssets value : values()) {
            if (value.fileName.equals(name)) {
                return value;
            }
        }
        return null;
    }

    private static List<Predicate<?>> getTrims(String armorType) {
        String[] materials = {"quartz", "iron", "netherite", "redstone", "copper", "gold", "emerald", "diamond", "lapis", "amethyst"};
        List<Predicate<?>> trims = new ArrayList<>();
        for (int i = 0; i < materials.length; i++) {
            float trim = BigDecimal.valueOf((i + 1) * 0.1).setScale(1, RoundingMode.HALF_UP).floatValue();
            trims.add(new Predicate<>(trim, PredicateType.TRIM_TYPE, "minecraft:item/" + armorType + "_" + materials[i] + "_trim"));
        }
        return trims;
    }
}
