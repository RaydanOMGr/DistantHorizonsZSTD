package me.andreasmelone.distanthorizonszstd;

import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

import java.io.*;

public class Util {
    private static final Logger LOGGER = LogUtils.getLogger();

    public static void extractFile(String pathInJar, File file) {
        try {
            extractFile(pathInJar, new FileOutputStream(file));
        } catch (FileNotFoundException e) {
            LOGGER.error("Failed to open file!", e);
        }
    }

    public static void extractFile(String pathInJar, OutputStream dest) {
        try(InputStream in = Util.class.getResourceAsStream(pathInJar)) {
            if(in == null) {
                LOGGER.error("Failed to open {}", pathInJar);
                return;
            }
            in.transferTo(dest);
        } catch (IOException e) {
            LOGGER.error("Failed to extract library!", e);
        }
    }
}
