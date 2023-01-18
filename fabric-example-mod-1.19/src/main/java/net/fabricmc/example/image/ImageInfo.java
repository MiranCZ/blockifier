package net.fabricmc.example.image;

import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.util.Identifier;

public record ImageInfo(NativeImageBackedTexture image, int[] averageColor, Identifier id) {
}
