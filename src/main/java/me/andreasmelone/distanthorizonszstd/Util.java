package me.andreasmelone.distanthorizonszstd;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Util {
    private static final Logger LOGGER = Logger.getLogger(Util.class.getName());

    public static void extractFile(String pathInJar, File file) {
        try {
            extractFile(pathInJar, new FileOutputStream(file));
        } catch (FileNotFoundException e) {
            LOGGER.log(Level.WARNING, "Failed to open file!", e);
        }
    }

    public static void extractFile(String pathInJar, OutputStream dest) {
        try(InputStream in = Util.class.getResourceAsStream(pathInJar)) {
            if(in == null) {
                LOGGER.warning("Failed to open " + pathInJar);
                return;
            }
            in.transferTo(dest);
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Failed to extract library!", e);
        }
    }
}
