package net.fabricmc.example;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static org.joml.Math.floor;

public class test {

    public static void main(String[] args) throws IOException {
        BufferedImage sampler = ImageIO.read(new File("C:\\Users\\Guest\\Downloads\\fabric-example-mod-1.19\\fabric-example-mod-1.19\\src\\main\\resources\\assets\\minecraft\\textures\\effect\\computed.png"));
        BufferedImage transform = ImageIO.read(new File("C:\\Users\\Guest\\Downloads\\fabric-example-mod-1.19\\fabric-example-mod-1.19\\run\\screenshots\\2023-01-21_15.39.36.png"));

        BufferedImage result = new BufferedImage(transform.getWidth(), transform.getHeight(),BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = result.createGraphics();

        for (int x = 0; x < result.getWidth(); x++) {
            for (int y = 0; y < result.getHeight(); y++) {
                Color c = new Color(transform.getRGB(x,y));
                int r = c.getRed()/2;
                int g = c.getGreen()/2;
                int b = c.getBlue()/2;

                int rgb = (r << 14) | (g << 7) | b;

                int texturePos = rgb/2;


                int xPos = texturePos % sampler.getWidth();
                int yPos = texturePos/sampler.getHeight();

                int newRgb = sampler.getRGB(xPos, yPos);

                g2d.setColor(new Color(newRgb));
                g2d.fillRect(x,y,1,1);
            }
        }
        g2d.dispose();
        ImageIO.write(result, "PNG", new File("result.png"));

    }

}
