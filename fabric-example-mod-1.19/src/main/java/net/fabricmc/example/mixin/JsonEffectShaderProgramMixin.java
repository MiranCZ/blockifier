package net.fabricmc.example.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.example.ExampleMod;
import net.minecraft.client.gl.GlUniform;
import net.minecraft.client.gl.JsonEffectShaderProgram;
import net.minecraft.client.gl.PostEffectProcessor;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Map;

import static net.fabricmc.example.ExampleMod.TEXTURE_SIZE;
import static net.fabricmc.example.ExampleMod.sizeList;

@Mixin(PostEffectProcessor.class)
public class JsonEffectShaderProgramMixin {

    @ModifyVariable(method =  "parseUniform", at = @At("STORE"), ordinal = 0)
    private GlUniform captureUniform(GlUniform uniform) {
        if (uniform.getName().equals("BlockSize")) {
            sizeList.add(uniform);
        }
        return uniform;
    }

}
