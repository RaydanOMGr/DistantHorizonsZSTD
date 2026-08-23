package me.andreasmelone.distanthorizonszstd.mixin;

import com.mojang.logging.LogUtils;
import dhcomgithubluben.zstd.util.Native;
import me.andreasmelone.distanthorizonszstd.AndroidLibLoader;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Mixin(Native.class)
public class ZstdNativeMixin {
    @Shadow
    private static AtomicBoolean loaded;

    @Shadow
    private static void loadLibraryFile(String string) {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    @Shadow
    @Final
    private static String libnameShort;

    @Inject(
            method = "load(Ljava/io/File;)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void injectLoad(File file, CallbackInfo ci) {
        if(!loaded.get() && AndroidLibLoader.INSTANCE.init()) {
            try {
                loadLibraryFile(AndroidLibLoader.INSTANCE.get(libnameShort));
                loaded.set(true);
                ci.cancel();
            } catch (Exception e) {
                LogUtils.getLogger().error("Failed to load android zstd", e);
                return;
            }
        }
    }
}
