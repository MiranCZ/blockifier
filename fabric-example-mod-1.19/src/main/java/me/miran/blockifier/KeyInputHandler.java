package me.miran.blockifier;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

import static me.miran.blockifier.Main.ENABLED;

public class KeyInputHandler {


    private static boolean prevPressed = false;
    public static void update() {
        boolean pressed = InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), GLFW.GLFW_KEY_RIGHT_SHIFT);

        if (!prevPressed && pressed) {
            ENABLED = !ENABLED;
        }
        prevPressed = pressed;
    }
}
