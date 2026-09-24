package me.andreasmelone.distanthorizonszstd.mixin;

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
import java.util.logging.Level;
import java.util.logging.Logger;

@Mixin(Native.class)
public class ZstdNativeMixin {
    @Shadow(remap = false)
    private static AtomicBoolean loaded;

    @Shadow(remap = false)
    private static void loadLibraryFile(String string) {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    @Shadow(remap = false)
    @Final
    private static String libnameShort;

    @Inject(
            method = "load(Ljava/io/File;)V",
            at = @At("HEAD"),
            cancellable = true,
            remap = false
    )
    private static void injectLoad(File file, CallbackInfo ci) {
        if(!loaded.get() && AndroidLibLoader.INSTANCE.init()) {
            try {
                loadLibraryFile(AndroidLibLoader.INSTANCE.get(libnameShort));
                loaded.set(true);
                ci.cancel();
            } catch (Exception e) {
                Logger.getLogger("ZstdNativeMixin").log(Level.WARNING, "Failed to load android zstd", e);
                return;
            }
        }
    }
}
