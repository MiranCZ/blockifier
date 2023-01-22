package net.fabricmc;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class creator {

    public static void main(String[] args) throws IOException {
        BufferedImage finalImg = new BufferedImage(1449, 1449, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = finalImg.createGraphics();

        BufferedImage palette = ImageIO.read(new File("C:\\Users\\Guest\\Downloads\\fabric-example-mod-1.19\\fabric-example-mod-1.19\\src\\main\\resources\\assets\\minecraft\\textures\\effect\\palette.png"));
        int[] rg = new int[2];
        for (int r = 0; r < 128; r++) {
            for (int g = 0; g < 128; g++) {
                for (int b = 0; b < 128; b++) {
                    int pos = searchInPalette(palette, new Color(r * 2, g * 2, b * 2));
                    int i = (r << 14) | (g << 7) | b;

                    int xPos = i % 1449;
                    int yPos = i / 1449;

                    int val1;
                    int val2 = 0;

                    if (pos < 256) {
                        val1 = pos;
                    } else {
                        val1 = 255;
                        val2 = pos - 255;
                    }
                   // g2d.setColor(new Color(r * 2, g * 2, b * 2));
                    g2d.setColor(new Color(val1,val2,0));
                    g2d.fillRect(xPos, yPos, 1, 1);

                }

            }
        }

        g2d.dispose();
        ImageIO.write(finalImg, "PNG", new File("computed.png"));
    }

    private static int searchInPalette(BufferedImage palette, Color color) {
        int bestMatch = Integer.MAX_VALUE;
        int bestMatchPos = 0;

        for (int i = 0; i < palette.getWidth(); i++) {
            Color c = new Color(palette.getRGB(i, 0));
            int r = Math.abs(color.getRed() - c.getRed());
            int g = Math.abs(color.getGreen() - c.getGreen());
            int b = Math.abs(color.getBlue() - c.getBlue());

            int result = r + g + b;
            if (result < bestMatch) {
                bestMatch = result;
                bestMatchPos = i;
            }
        }

        return bestMatchPos;
    }


}
