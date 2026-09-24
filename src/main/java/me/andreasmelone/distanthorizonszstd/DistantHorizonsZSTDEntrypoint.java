// an entrypoint is only required on forge and early neoforge
//? if forgelike {
/*package me.andreasmelone.distanthorizonszstd;

//~ if neoforge 'net.minecraftforge.fml.' -> 'net.neoforged.fml.'
import net.minecraftforge.fml.common.Mod;

@Mod(value = "distanthorizonszstd" /^? if forge { ^/ /^, modid = "distanthorizonszstd" ^//^? }^/)
public class DistantHorizonsZSTDEntrypoint {
}
*///? }