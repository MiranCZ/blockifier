package net.fabricmc.example.event;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

import static net.fabricmc.example.ExampleMod.ENABLED;

public class KeyInputHandler {


    private static boolean prevPressed = false;
    public static void update() {
        boolean pressed = InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), GLFW.GLFW_KEY_M);

        if (!prevPressed && pressed) {
            ENABLED = !ENABLED;
        }
        prevPressed = pressed;
    }
}
