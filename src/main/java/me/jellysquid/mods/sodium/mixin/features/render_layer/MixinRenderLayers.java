package me.jellysquid.mods.sodium.mixin.features.render_layer;

import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

@Mixin(ItemBlockRenderTypes.class)
public class MixinRenderLayers {
    @Mutable
    @Shadow
    @Final
    private static Map<Block, RenderType> BLOCKS;

    @Mutable
    @Shadow
    @Final
    private static Map<Fluid, RenderType> FLUIDS;

    static {
        // Replace the backing collection types with something a bit faster, since this is a hot spot in chunk rendering.
        BLOCKS = new Reference2ReferenceOpenHashMap<>(BLOCKS);
        FLUIDS = new Reference2ReferenceOpenHashMap<>(FLUIDS);
    }
}
