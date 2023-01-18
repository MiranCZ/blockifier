package net.fabricmc.example.mixin;


import net.fabricmc.example.TextureSizeSlider;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.widget.GridWidget.Adder;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import static net.fabricmc.example.ExampleMod.TEXTURE_SIZE;


@Mixin(OptionsScreen.class)
public class OptionsScreenMixin {

    @ModifyVariable(method = "init", at = @At("STORE"), ordinal = 0)
    public Adder addButton(Adder adder) {
        adder.add(new TextureSizeSlider(0,300,150,20, Text.literal("1:" + TEXTURE_SIZE),Math.round(TEXTURE_SIZE/16f)));
        return adder;
    }

}
