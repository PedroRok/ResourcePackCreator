package com.pedrorok.core.config;

import com.pedrorok.core.RPCore;
import lombok.AccessLevel;
import lombok.Getter;
import org.simpleyaml.configuration.file.YamlFile;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Rok, Pedro Lucas nmm. Created on 28/09/2024
 * @project BrPacks
 */
@Getter
public class RPConfig {

    @Getter(value = AccessLevel.PRIVATE)
    private final YamlFile yamlFile;

    private String customizationFolder;
    private String iconFile;
    private String packName;
    private int packVersion;
    private String packDescription;

    private Map<String, ArmorConfig> registeredArmors;


    public RPConfig() {
        yamlFile = new YamlFile("resources/config.yml");
        try {
            yamlFile.load();
        } catch (Exception e) {
            RPCore.LOGGER.error("Failed to load config file: {}", e.getMessage());
        }
        yamlFile.addDefault("pack-info.customization-folder", "rokpack");
        yamlFile.addDefault("pack-info.icon", "pack.png");
        yamlFile.addDefault("pack-info.name", "RPCreator - By Rok");
        yamlFile.addDefault("pack-info.description", "A simple resource pack creator");

        try {
            if (!yamlFile.exists()) {
                yamlFile.save();
            }
        } catch (Exception e) {
            RPCore.LOGGER.error("Failed to save config file: {}", e.getMessage());
        }
    }

    public void init() {
        customizationFolder = yamlFile.getString("pack-info.customization-folder");
        iconFile = yamlFile.getString("pack-info.icon");
        packName = yamlFile.getString("pack-info.name");
        packDescription = yamlFile.getString("pack-info.description");
        packVersion = yamlFile.getInt("pack-info.version");
        importRegisteredArmors();
    }

    public void importRegisteredArmors() {
        if (!yamlFile.contains("armors")) return;
        registeredArmors = new HashMap<>();
        Set<String> armors = yamlFile.getConfigurationSection("armors").getKeys(false);
        for (String armor : armors) {
            int model = -1;
            Color color = null;
            if (yamlFile.contains("armors." + armor + ".model")) {
                model = yamlFile.getInt("armors." + armor + ".model");
            }
            if (yamlFile.contains("armors." + armor + ".color")) {
                String colorString = yamlFile.getString("armors." + armor + ".color");
                colorString = colorString.replace("#", "");
                try {
                    color = new Color(Integer.parseInt(colorString, 16));
                } catch (NumberFormatException e) {
                    RPCore.LOGGER.error("Invalid color for armor: {}", armor);
                }
            }
            RPCore.LOGGER.info("Importing armor: {}", armor);
            ArmorConfig armorConfig = new ArmorConfig(model, color);
            registeredArmors.put(armor, armorConfig);
        }
    }

    public ArmorConfig getArmorConfig(String armor) {
        if (registeredArmors == null) return null;
        return registeredArmors.get(armor);
    }

}
