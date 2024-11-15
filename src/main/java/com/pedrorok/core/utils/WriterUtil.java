package com.pedrorok.core.utils;

import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @author Rok, Pedro Lucas nmm. Created on 14/11/2024
 * @project ResourcePackCreator
 */
public class WriterUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger("Writer");

    /**
     * Write an image to a file
     *
     * @param mainFolder    The main folder, if null, it will be the default "minecraft" folder
     * @param path          The path
     * @param fileName      The file name
     * @param bufferedImage The image
     */
    public static void writeImage(@Nullable String mainFolder, String path, String fileName, BufferedImage bufferedImage) {
        try {
            StringBuilder builder = new StringBuilder("output/assets/");
            builder.append(mainFolder == null ? "minecraft" : mainFolder);
            builder.append("/textures/");
            if (!path.endsWith("/")) path += "/";
            builder.append(path);
            if (!fileName.endsWith(".png")) fileName += ".png";
            builder.append(fileName);

            File output = WriterUtil.createFile(builder.toString());
            ImageIO.write(bufferedImage, "png", output);
        } catch (IOException e) {
            LOGGER.error("Failed to write image to file: {}", e.getMessage());
        }
    }

    public static File createFile(String path) {
        File file = new File(path);
        if (!file.exists()) {
            boolean mkdir = file.getParentFile().mkdirs();
            if (mkdir) {
                LOGGER.debug("Created the {} file.", file.getName());
            }
        }
        return file;
    }

}
