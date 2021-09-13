package me.jellysquid.mods.sodium.mixin.features.shader;

import com.mojang.blaze3d.shaders.Uniform;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.client.renderer.ShaderInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ShaderInstance.class)
public class MixinShader {
    private final Object2IntMap<CharSequence> uniformLocationCache = new Object2IntOpenHashMap<>();

    public MixinShader() {
        this.uniformLocationCache.defaultReturnValue(-1);
    }

    @Redirect(method = "upload", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gl/GlUniform;getUniformLocation(ILjava/lang/CharSequence;)I"))
    private int redirectGetUniformLocation(int program, CharSequence name) {
        int id = this.uniformLocationCache.computeIfAbsent(name, (key) -> Uniform.glGetUniformLocation(program, key));

        if (id < 0) {
            throw new NullPointerException("Couldn't find uniform " + name + "for program " + program);
        }

        return id;
    }
}
