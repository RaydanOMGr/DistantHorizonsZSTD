package me.andreasmelone.distanthorizonszstd.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.logging.LogUtils;
import dh_sqlite.SQLiteJDBCLoader;
import me.andreasmelone.distanthorizonszstd.AndroidLibLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SQLiteJDBCLoader.class)
public class SQLiteJDBCLoaderMixin {
    @WrapOperation(
            method = "loadSQLiteNativeLibrary",
            at = @At(value = "INVOKE", target = "Ldh_sqlite/SQLiteJDBCLoader;loadNativeLibraryJdk()Z", ordinal = 0)
    )
    private static boolean loadNativeLib(Operation<Boolean> original) {
        if(AndroidLibLoader.INSTANCE.init()) {
            try {
                System.load(AndroidLibLoader.INSTANCE.get("sqlitejdbc"));
                return true;
            } catch (Exception e) {
                LogUtils.getLogger().error("Failed to load android sqlite", e);
            }
        }

        return original.call();
    }
}
