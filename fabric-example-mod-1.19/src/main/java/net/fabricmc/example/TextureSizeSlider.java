package net.fabricmc.example;

import net.fabricmc.example.image.ImageHelper;
import net.fabricmc.example.image.SampleImages;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;

import static net.fabricmc.example.ExampleMod.NEW_TEXTURE_SIZE;


public class TextureSizeSlider extends SliderWidget {

    public TextureSizeSlider(int x, int y, int width, int height, Text text, double value) {
        super(x, y, width, height, text, value);
    }

    @Override
    protected void updateMessage() {
        this.setMessage(Text.literal("1:" + valueToSize()));
    }

    @Override
    protected void applyValue() {
        NEW_TEXTURE_SIZE = valueToSize();
    }

    private int valueToSize() {
        int size = (int) (value * 16) + 1;
        if (size > 16) size = 16;

        return size;
    }
}
