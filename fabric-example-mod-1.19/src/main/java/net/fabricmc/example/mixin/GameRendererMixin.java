package net.fabricmc.example.mixin;


import net.fabricmc.example.event.KeyInputHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.PostEffectProcessor;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.Window;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec2f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;

import static net.fabricmc.example.ExampleMod.*;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {

    @Shadow @Final MinecraftClient client;
    private PostEffectProcessor test = null;
    private Vec2f prevDimensions = new Vec2f(0,0);

    @Inject(method = "render", at = @At("TAIL"))
    private void render(float tickDelta, long startTime, boolean tick, CallbackInfo ci) {
        KeyInputHandler.update();
        if (TEXTURE_SIZE != NEW_TEXTURE_SIZE) {
            TEXTURE_SIZE = NEW_TEXTURE_SIZE;
        }

        MinecraftClient mc = MinecraftClient.getInstance();
        if (test == null) {
            try {
                test = new PostEffectProcessor(mc.getTextureManager(),mc.getResourceManager(),mc.getFramebuffer(),new Identifier("modid","shaders/post/shadertest.json"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        Window window = this.client.getWindow();
        if (prevDimensions.x != this.client.getWindow().getFramebufferWidth() || prevDimensions.y != this.client.getWindow().getFramebufferHeight()) {
            test.setupDimensions(window.getFramebufferWidth(), window.getFramebufferHeight());
            prevDimensions = new Vec2f(window.getFramebufferWidth(),window.getFramebufferHeight());
        }

        if (ENABLED) {
            test.render(0);
        }
    }




}
