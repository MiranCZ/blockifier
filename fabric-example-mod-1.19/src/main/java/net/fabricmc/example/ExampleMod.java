package net.fabricmc.example;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.render.*;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExampleMod implements ModInitializer {

	public static final Logger LOGGER = LoggerFactory.getLogger("modid");
	public static final Identifier textureId = new Identifier("custombackground");

	public static int TEXTURE_SIZE = 8;
	public static int NEW_TEXTURE_SIZE = 8;
	public static boolean ENABLED = false;

	public static NativeImageBackedTexture OVERLAY_TEXTURE;

	@Override
	public void onInitialize() {
	}

	public static void renderTexture(int x, int y, int width, int height,Identifier id) {
		RenderSystem.disableDepthTest();
		RenderSystem.depthMask(false);
		RenderSystem.defaultBlendFunc();
		RenderSystem.setShader(GameRenderer::getPositionTexProgram);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, id);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferBuilder = tessellator.getBuffer();
		bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
		bufferBuilder.vertex(x,y+ height, 0).texture(0.0F, 1.0F).next();
		bufferBuilder.vertex(x+width,y+ height, 0).texture(1.0F, 1.0F).next();
		bufferBuilder.vertex(x+width, y, 0).texture(1.0F, 0.0F).next();
		bufferBuilder.vertex(x, y, 0).texture(0.0F, 0.0F).next();
		tessellator.draw();
		RenderSystem.depthMask(true);
		RenderSystem.enableDepthTest();
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
	}



}
