package net.fabricmc.example.mixin;


import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.example.ExampleMod;
import net.fabricmc.example.event.KeyInputHandler;
import net.fabricmc.example.image.ImageHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.PostEffectPass;
import net.minecraft.client.gl.PostEffectProcessor;
import net.minecraft.client.gl.SimpleFramebuffer;
import net.minecraft.client.render.*;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;

import static net.fabricmc.example.ExampleMod.*;
import static net.fabricmc.example.image.ImageHelper.time;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {

    @Shadow @Final
    static Logger LOGGER;
    @Shadow private boolean postProcessorEnabled;
    @Shadow private @Nullable PostEffectProcessor postProcessor;
    @Shadow @Final private MinecraftClient client;
    private PostEffectProcessor test = null;

    @Inject(method = "render", at = @At("TAIL"))
    private void render(float tickDelta, long startTime, boolean tick, CallbackInfo ci) {
        KeyInputHandler.update();
        if (ENABLED) {
            updateOverlay();
         //   ExampleMod.renderTexture(0,0,100,100,textureId);
         //   renderOverlay();
        }
        if (TEXTURE_SIZE != NEW_TEXTURE_SIZE) {
            TEXTURE_SIZE = NEW_TEXTURE_SIZE;
            ImageHelper.resetCache();
        }

        MinecraftClient mc = MinecraftClient.getInstance();
        if (test == null) {
            try {
                test = new PostEffectProcessor(mc.getTextureManager(),mc.getResourceManager(),mc.getFramebuffer(),new Identifier("modid","shaders/post/shadertest.json"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

       // if (client.world != null) {
        //test.setupDimensions(this.client.getWindow().getFramebufferWidth(), this.client.getWindow().getFramebufferHeight());
       // test.render(0);
        //}
      //  this.postProcessorEnabled = true;
        //this.postProcessor = test;
    }

    private void renderOverlay() {
        MinecraftClient mc = MinecraftClient.getInstance();

        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, textureId);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
        bufferBuilder.vertex(0.0D, mc.getWindow().getScaledHeight(), -180.0D).texture(0.0F, 1.0F).next();
        bufferBuilder.vertex(mc.getWindow().getScaledWidth(), mc.getWindow().getScaledHeight(), -180.0D).texture(1.0F, 1.0F).next();
        bufferBuilder.vertex(mc.getWindow().getScaledWidth(), 0.0D, -180.0D).texture(1.0F, 0.0F).next();
        bufferBuilder.vertex(0.0D, 0.0D, -180.0D).texture(0.0F, 0.0F).next();
        tessellator.draw();
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();

    }

    private void updateOverlay() {
        if (OVERLAY_TEXTURE == null) {
            OVERLAY_TEXTURE = new NativeImageBackedTexture(new NativeImage(1,1,false));
        }
        MinecraftClient mc = MinecraftClient.getInstance();
        long millis1 = System.currentTimeMillis();
        NativeImage img = bufferToNativeImage(mc.getFramebuffer());
        long millis1_result = System.currentTimeMillis()-millis1;
        if (img == null) return;
        long millis2 = System.currentTimeMillis();
        for (int i = 0; i < img.getWidth(); i += TEXTURE_SIZE) {
            for (int j = 0; j < img.getHeight(); j+=TEXTURE_SIZE) {
                ImageHelper.changeToTexture(img, i, j);
            }
        }


        long millis2_result = System.currentTimeMillis()-millis2;

        OVERLAY_TEXTURE.clearGlId();
        OVERLAY_TEXTURE = new NativeImageBackedTexture(img);
        //taking image: +- 15ms
        LOGGER.info("load: " + millis1_result);
        time = 0;
       // LOGGER.info("taking image: " + millis1_result + "; making block textures: " + millis2_result);
    }
    private NativeImage img = new NativeImage(1,1,false);
    private NativeImage bufferToNativeImage(Framebuffer framebuffer) {
        int i = framebuffer.textureWidth;
        int j = framebuffer.textureHeight;

        if (img.getWidth() != i || img.getHeight() != j) {
            img = new NativeImage(i, j, false);
        }

        RenderSystem.bindTexture(framebuffer.getColorAttachment());
        img.loadFromTextureImage(0,false);//this costs all the ms

        img.mirrorVertically();

        return img;
    }




}
