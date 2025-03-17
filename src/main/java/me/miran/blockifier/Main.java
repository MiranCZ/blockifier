package me.miran.blockifier;

import net.fabricmc.api.ModInitializer;
import net.minecraft.client.gl.GlUniform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class Main implements ModInitializer {

	public static final Logger LOGGER = LoggerFactory.getLogger("blockifier");

	public static int TEXTURE_SIZE = 8;
	public static int NEW_TEXTURE_SIZE = 8;
	public static boolean ENABLED = true;

	public static List<GlUniform> sizeList = new ArrayList<>();

	@Override
	public void onInitialize() {
	}

}
