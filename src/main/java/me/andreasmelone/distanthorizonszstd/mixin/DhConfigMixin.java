package me.andreasmelone.distanthorizonszstd.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(targets = "com.seibel.distanthorizons.core.config.Config$Client$Advanced$Graphics$Quality")
public class DhConfigMixin {
    @ModifyArg(
            method = "<clinit>",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/seibel/distanthorizons/core/config/types/ConfigEntry$Builder;setMinDefaultMax(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Lcom/seibel/distanthorizons/core/config/types/ConfigEntry$Builder;",
                    ordinal = 0
            ),
            index = 0,
            require = 0,
            allow = 1,
            expect = 0,
            remap = false
    )
    private static <T> T setMinDistanceToTwo(T min) {
        //noinspection unchecked
        return (T)(Integer)2;
    }
}
