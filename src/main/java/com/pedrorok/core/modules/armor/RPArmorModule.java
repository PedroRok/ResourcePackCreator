package com.pedrorok.core.modules.armor;

import com.pedrorok.core.RPCore;
import com.pedrorok.core.modules.item.RPItemModule;
import com.pedrorok.core.utils.WriterUtil;

import java.io.File;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;

/**
 * @author Rok, Pedro Lucas nmm. Created on 12/11/2024
 * @project ResourcePackCreator
 */
public class RPArmorModule {

    private final RPItemModule itemModule;
    private final Map<String, ArmorEntity> armors = new HashMap<>();
    private final String customFolder;

    public RPArmorModule(File armorFolder, RPItemModule itemModule, String customFolder) {
        this.itemModule = itemModule;
        RPCore.LOGGER.info("Searching for armors...");
        this.customFolder = customFolder;
        searchArmors(armorFolder, customFolder + ":armor/");
    }

    private void searchArmors(File dir, String subDir) {
        for (File armorDir : dir.listFiles()) {
            if (armorDir.isDirectory()) {
                searchArmors(armorDir, subDir + armorDir.getName() + "/");
                continue;
            }
            ArmorEntity armor = importArmor(dir);
            if (armor == null) break;
            armors.put(subDir, armor);
            break;
        }
    }

    private ArmorEntity importArmor(File dir) {
        Map<String, BufferedImage> images = new HashMap<>();
        File[] files = dir.listFiles();
        if (files == null) {
            RPCore.LOGGER.error("No files found in armor folder: {}", dir.getName());
            return null;
        }
        for (File file : files) {
            String name = file.getName().replace(".png", "");
            try {
                BufferedImage image = ImageIO.read(file);
                if (name.contains("layer_1")) {
                    name = "layer_1";
                }
                if (name.contains("layer_2")) {
                    name = "layer_2";
                }
                images.put(name, image);
            } catch (IOException e) {
                RPCore.LOGGER.error("Error reading image: {}", file.getName());
            }
        }
        if (images.isEmpty()) {
            RPCore.LOGGER.error("No images found in armor folder: {}", dir.getName());
            return null;
        }
        if (!images.containsKey("layer_1") || !images.containsKey("layer_2")) {
            RPCore.LOGGER.error("Missing layer1 or layer2 image in armor folder: {}", dir.getName());
            return null;
        }
        ArmorEntity arm = new ArmorEntity(dir.getName().replace(".png", ""), images);
        //RPCore.LOGGER.info("Found armor: {}, with color: {}", dir.getName(), "#" + Integer.toHexString(arm.color().getRGB() & 0xFFFFFF));
        return arm;
    }

    public void createArmorItems() {
        armors.forEach((path, armor) -> {
            int model = -1;
            for (String fileName : armor.images().keySet()) {
                String fName = fileName.toLowerCase();

                if (fName.contains("helmet")) {
                    model = itemModule.addItemCustomModel("leather_helmet", path + fileName);
                } else if (fName.contains("chestplate")) {
                    model = itemModule.addItemCustomModel("leather_chestplate", path + fileName);
                } else if (fName.contains("leggings")) {
                    model = itemModule.addItemCustomModel("leather_leggings", path + fileName);
                } else if (fName.contains("boots")) {
                    model = itemModule.addItemCustomModel("leather_boots", path + fileName);
                }
            }
            RPCore.LOGGER.info("Added armor: {} \\ with model: {} \\ with color: {}", armor.name(), model, "#" + Integer.toHexString(armor.color().getRGB() & 0xFFFFFF));
            armor.generateItemsFile(path, customFolder);
        });
        generateImages(true);
        generateImages(false);

        WriterUtil.writeImage(customFolder, "item/armor", "transparent", new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB));
        WriterUtil.writeImage(null, "models/armor", "leather_layer_1_overlay", new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB));
        WriterUtil.writeImage(null, "models/armor", "leather_layer_2_overlay", new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB));
    }

    public void generateImages(boolean layer1) {
        for (ArmorEntity value : armors.values()) {
            BufferedImage layer = value.images().get("layer_" + (layer1 ? "1" : "2"));
            if (layer.getWidth() != 128 || layer.getHeight() != 64) {
                layer = resizeImage(layer, 128, 64);
            }
            value.images().put("layer_" + (layer1 ? "1" : "2"), layer);
        }
        BufferedImage bufferedImage = combineImages(layer1);
        WriterUtil.writeImage(null, "models/armor", "leather_layer_" + (layer1 ? "1" : "2"), bufferedImage);
    }

    private BufferedImage resizeImage(BufferedImage original, int width, int height) {
        BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resized.createGraphics();
        g2d.setComposite(AlphaComposite.Src);
        g2d.drawImage(original, 0, 0, width, height, null);
        g2d.dispose();
        return resized;
    }

    private BufferedImage combineImages(boolean layer1) {

        BufferedImage defaultLayer = null;
        try {
            defaultLayer = ImageIO.read(getClass().getClassLoader().getResourceAsStream("default/minecraft/textures/models/armor/leather_layer_"+ (layer1 ? "1" : "2") +".png"));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        Map<BufferedImage, Color> images = new LinkedHashMap<>();
        images.put(defaultLayer, null);
        images.put(null, null);
        for (ArmorEntity value : armors.values()) {
            images.put(value.images().get("layer_" + (layer1 ? "1" : "2")), value.color());
        }

        int totalWidth = (images.size() * 128); // 128 é a largura após o redimensionamento
        int height = 64; // altura após o redimensionamento

        BufferedImage combined = new BufferedImage(totalWidth, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = combined.createGraphics();
        g2d.setComposite(AlphaComposite.Src);

        int currentX = 0;
        for (Map.Entry<BufferedImage, Color> img : images.entrySet()) {
            g2d.drawImage(img.getKey(), currentX, 0, null);
            // Coloca a cor média no primeiro pixel
            if (img.getValue() != null) {
                combined.setRGB(currentX, 0, img.getValue().getRGB());
                combined.setRGB(currentX + 2, 0, Color.BLACK.getRGB());
            }
            // Coloca o pixel preto no terceiro pixel

            currentX += 128;
        }

        g2d.dispose();
        return combined;
    }
}