package me.miran.blockifier.mixin;

import net.minecraft.client.gl.GlUniform;
import net.minecraft.client.gl.PostEffectProcessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import static me.miran.blockifier.Main.sizeList;

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
