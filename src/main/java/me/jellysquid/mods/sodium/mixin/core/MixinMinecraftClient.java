package me.jellysquid.mods.sodium.mixin.core;

import it.unimi.dsi.fastutil.longs.LongArrayFIFOQueue;
import me.jellysquid.mods.sodium.client.SodiumClientMod;
import me.jellysquid.mods.sodium.client.gui.screen.ConfigCorruptedScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.main.GameConfig;
import org.lwjgl.opengl.GL32C;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MixinMinecraftClient {
    private final LongArrayFIFOQueue fences = new LongArrayFIFOQueue();

    @Inject(method = "<init>", at = @At("RETURN"))
    private void postInit(GameConfig args, CallbackInfo ci) {
        if (SodiumClientMod.options().isReadOnly()) {
            Screen parent = Minecraft.getInstance().screen;
            Minecraft.getInstance().setScreen(new ConfigCorruptedScreen(() -> parent));
        }
    }

    @Inject(method = "runTick", at = @At("HEAD"))
    private void preRender(boolean tick, CallbackInfo ci) {
        while (this.fences.size() > SodiumClientMod.options().advanced.maxPreRenderedFrames) {
            GL32C.glClientWaitSync(this.fences.dequeueLong(), GL32C.GL_SYNC_FLUSH_COMMANDS_BIT, Long.MAX_VALUE);
        }
    }

    @Inject(method = "runTick", at = @At("RETURN"))
    private void postRender(boolean tick, CallbackInfo ci) {
        this.fences.enqueue(GL32C.glFenceSync(GL32C.GL_SYNC_GPU_COMMANDS_COMPLETE, 0));
    }
}
