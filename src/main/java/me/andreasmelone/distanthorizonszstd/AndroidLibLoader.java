package me.andreasmelone.distanthorizonszstd;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AndroidLibLoader {
    public static final AndroidLibLoader INSTANCE = new AndroidLibLoader();

    private String path = "";
    private boolean initialized = false;

    public boolean init() {
        if(this.initialized) return true;

        String packageName = null;
        String userId = null;

        Pattern pattern = Pattern.compile("^/data/user/(\\d+)/([^/]+)/.*");
        for (String path : System.getProperty("java.library.path").split(File.pathSeparator)) {
            Matcher m = pattern.matcher(path);
            if (!m.find()) continue;
            userId = m.group(1);
            packageName = m.group(2);
            break;
        }

        if(packageName == null || userId == null) return false;

        AndroidLibLoader.INSTANCE.setPath("/data/user/" + userId + "/" + packageName + "/cache");
        return this.initialized = true;
    }

    public String get(String name) {
        File file = new File(path + "/extracted_library/lib" + name + ".so");
        file.getParentFile().mkdirs();
        String pathInJar = "/natives/" + getArch() + "/lib" + name + ".so";
        Util.extractFile(pathInJar, file);
        return file.getPath();
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    private static String getArch() {
        String arch = System.getProperty("os.arch").toLowerCase();
        return switch (arch) {
            case "x86" -> "x86";
            case "amd64", "x86_64" -> "x86_64";
            case "arm", "armv7", "armv7l" -> "armeabi-v7a";
            case "aarch64", "arm64" -> "arm64-v8a";
            default -> throw new UnsupportedOperationException("Unknown architecture: " + arch);
        };
    }
}
