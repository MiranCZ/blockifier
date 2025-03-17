package me.miran.blockifier.mixin;


import me.miran.blockifier.TextureSizeSlider;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.widget.GridWidget.Adder;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Map;

import static me.miran.blockifier.Main.NEW_TEXTURE_SIZE;


@Mixin(OptionsScreen.class)
public class OptionsScreenMixin {

    @ModifyVariable(method = "init", at = @At("STORE"), ordinal = 0)
    public Adder addButton(Adder adder) {
        Map<Integer, Float> values = Map.of(
                1, 0f,
                2, 0.21f,
                4, 0.41f,
                8, 0.61f,
                16, 1f
        );
        adder.add(new TextureSizeSlider(0,300,150,20, Text.literal("1:" + NEW_TEXTURE_SIZE),values.get(NEW_TEXTURE_SIZE)));
        return adder;
    }

}
