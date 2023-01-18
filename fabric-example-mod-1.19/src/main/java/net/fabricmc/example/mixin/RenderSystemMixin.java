package net.fabricmc.example.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.example.ExampleMod;
import net.fabricmc.example.image.ImageHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.AbstractTexture;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import static net.fabricmc.example.ExampleMod.OVERLAY_TEXTURE;
import static net.fabricmc.example.ExampleMod.textureId;


@Mixin(RenderSystem.class)
public abstract class RenderSystemMixin {

    @Final
    @Shadow
    private static int[] shaderTextures;

    /**
     * @author
     * @reason
     */
    @Overwrite
    public static void _setShaderTexture(int texture, Identifier id) {
        if (id.getNamespace().equals("blockifier")) {
            int i = Integer.parseInt(id.getPath());
          //  if (OVERLAY_TEXTURE == null) return;
            shaderTextures[texture] = ImageHelper.sampleImages.getImageInfoList().get(i).image().getGlId();
            return;
        }
        if (texture >= 0 && texture < shaderTextures.length) {
            TextureManager textureManager = MinecraftClient.getInstance().getTextureManager();
            AbstractTexture abstractTexture = textureManager.getTexture(id);
            shaderTextures[texture] = abstractTexture.getGlId();
        }
    }
}
