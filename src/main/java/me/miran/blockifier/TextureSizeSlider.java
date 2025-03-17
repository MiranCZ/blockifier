package me.miran.blockifier;

import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;

import static me.miran.blockifier.Main.NEW_TEXTURE_SIZE;


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
        int size = (int) (value * 5);
        if (size == 5) size = 4;

        return (int) Math.pow(2,size);
    }
}
