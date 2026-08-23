package me.andreasmelone.distanthorizonszstd.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(targets = "com.seibel.distanthorizons.core.config.Config$Client$Advanced$Graphics$Quality")
public class DhConfigMixin {
    @ModifyArgs(
            method = "<clinit>",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/seibel/distanthorizons/core/config/types/ConfigEntry$Builder;setMinDefaultMax(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Lcom/seibel/distanthorizons/core/config/types/ConfigEntry$Builder;",
                    ordinal = 0
            ),
            require = 0,
            allow = 1,
            expect = 0
    )
    private static void setMinDistanceToTwo(Args args) {
        args.set(0, 2);
    }
}
