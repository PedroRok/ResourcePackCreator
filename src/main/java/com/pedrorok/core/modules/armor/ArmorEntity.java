package com.pedrorok.core.modules.armor;

import com.google.gson.JsonObject;
import com.pedrorok.core.utils.JsonUtil;
import com.pedrorok.core.utils.WriterUtil;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Map;

/**
 * @author Rok, Pedro Lucas nmm. Created on 12/11/2024
 * @project ResourcePackCreator
 */
public record ArmorEntity(String name, Color color, Map<String, BufferedImage> images) {

    public ArmorEntity(String name, Map<String, BufferedImage> images) {
        this(name, calculateAverageColor(images.get("layer_1")), images);
    }


    private static Color calculateAverageColor(BufferedImage image) {
        long sumR = 0, sumG = 0, sumB = 0;
        int totalPixels = image.getWidth() * image.getHeight();

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                Color pixel = new Color(image.getRGB(x, y));
                sumR += pixel.getRed();
                sumG += pixel.getGreen();
                sumB += pixel.getBlue();
            }
        }

        int avgR = (int) (sumR / totalPixels);
        int avgG = (int) (sumG / totalPixels);
        int avgB = (int) (sumB / totalPixels);

        return new Color(avgR, avgG, avgB);
    }


    public void generateItemsFile(String path, String customPath) {
        images.forEach((name, img) -> {
            if (name.contains("layer")) return;

            String[] pathFile = path.split(":");
            WriterUtil.writeImage(pathFile[0], "item/" + pathFile[1], name, img);

            // Cria um elemento json e escreve as propriedades nele como objeto
            JsonObject jsonElement = new JsonObject();
            jsonElement.addProperty("parent", "minecraft:item/generated");
            JsonObject jsonTextures = new JsonObject();
            jsonTextures.addProperty("layer0", customPath + ":item/armor/transparent");
            jsonTextures.addProperty("layer1", path.replace("output/assets/", "").replace(":", ":item/") + name);
            jsonElement.add("textures", jsonTextures);
            JsonUtil.writeFile(pathFile[0], "item/" + pathFile[1], name, jsonElement);
        });
    }


}
