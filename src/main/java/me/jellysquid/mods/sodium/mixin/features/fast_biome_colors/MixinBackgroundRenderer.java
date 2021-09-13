package me.jellysquid.mods.sodium.mixin.features.fast_biome_colors;

import me.jellysquid.mods.sodium.client.util.color.FastCubicSampler;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.util.CubicSampler;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(FogRenderer.class)
public class MixinBackgroundRenderer {
    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/CubicSampler;sampleColor(Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/CubicSampler$RgbFetcher;)Lnet/minecraft/util/math/Vec3d;"))
    private static Vec3 redirectSampleColor(Vec3 pos, CubicSampler.Vec3Fetcher rgbFetcher, Camera camera, float tickDelta, ClientLevel world, int i, float f) {
        float u = Mth.clamp(Mth.cos(world.getTimeOfDay(tickDelta) * 6.2831855F) * 2.0F + 0.5F, 0.0F, 1.0F);

        return FastCubicSampler.sampleColor(pos,
                (x, y, z) -> world.getBiomeManager().getNoiseBiomeAtQuart(x, y, z).getFogColor(),
                (v) -> world.effects().getBrightnessDependentFogColor(v, u));
    }
}
