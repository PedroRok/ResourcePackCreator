package com.pedrorok.core;

import com.pedrorok.core.config.RPConfig;
import com.pedrorok.core.modules.armor.RPArmorModule;
import com.pedrorok.core.modules.item.RPItemModule;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * @author Rok, Pedro Lucas nmm. Created on 12/11/2024
 * @project ResourcePackCreator
 */
@Getter
public class RPCore {

    public static Logger LOGGER = LoggerFactory.getLogger("RPCore");

    private RPArmorModule armorModule = null;
    private RPItemModule itemModule = null;
    private RPConfig config;

    public RPCore() {
        LOGGER.info("Starting Resource pack creator...");
        File file = new File("resources");
        if (!file.exists() || !file.isDirectory()) {
            LOGGER.error("Resources folder not found, creating...");
            boolean mkdir = file.mkdirs();
            if (mkdir) {
                LOGGER.info("Resources folder created, please put the files you want to pack in the resources folder and run the program again.");
            }
            return;
        }

        config = new RPConfig();
        config.init();
        itemModule = new RPItemModule();

        for (File listFile : file.listFiles()) {
            if (listFile.getName().equals("armors"))
                armorModule = new RPArmorModule(listFile, this, config.getCustomizationFolder());
        }
        armorModule.createArmorItems();
    }

    public static void main(String[] args) {
        new RPCore();
    }

}
