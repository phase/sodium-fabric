package me.jellysquid.mods.sodium.mixin.features.fast_biome_colors;

import me.jellysquid.mods.sodium.client.util.color.FastCubicSampler;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.CubicSampler;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.Function;

@Mixin(ClientLevel.class)
public class MixinClientWorld {
    @Redirect(method = "method_23777", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/CubicSampler;sampleColor(Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/CubicSampler$RgbFetcher;)Lnet/minecraft/util/math/Vec3d;"))
    private Vec3 redirectSampleColor(Vec3 pos, CubicSampler.Vec3Fetcher rgbFetcher) {
        Level world = (Level) (Object) this;

        return FastCubicSampler.sampleColor(pos, (x, y, z) -> world.getNoiseBiome(x, y, z).getSkyColor(), Function.identity());
    }
}
