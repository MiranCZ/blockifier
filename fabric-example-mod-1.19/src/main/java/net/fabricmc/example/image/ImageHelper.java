package net.fabricmc.example.image;

import net.fabricmc.example.ExampleMod;
import net.minecraft.client.gl.PostEffectProcessor;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.util.Identifier;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;

import static net.fabricmc.example.ExampleMod.TEXTURE_SIZE;

public class ImageHelper {

    private ImageHelper() {
    }

    public static final SampleImages sampleImages = new SampleImages();

    public static int[] getAverageRGB(BufferedImage image) {
        int avR = 0;
        int avG = 0;
        int avB = 0;

        for (int x = 0; x < image.getWidth(); x++){
            for (int y = 0; y < image.getHeight(); y++) {
                int color = 0xff000000 | image.getRGB(x,y);

                avR += (color >> 16) & 0xFF;
                avG += (color >> 8) & 0xFF;
                avB += color & 0xFF;
            }
        }

        int samples = image.getWidth()*image.getHeight();
        int r = avR/samples; int g = avG/samples; int b = avB/samples;
        return new int[]{r,g,b};
    }

    public static BufferedImage changeRes(BufferedImage img, int width, int height) {
        Image tmp = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }

    public static int[] getAverageRGB(NativeImage image,int i, int j) {
        int avR = 0;
        int avG = 0;
        int avB = 0;

        int samples = 0;

        for (int x = i; x < i+TEXTURE_SIZE; x++){
            for (int y = j; y < j+TEXTURE_SIZE; y++) {
                if (x >= image.getWidth() || y >= image.getHeight()) continue;
                samples++;

                int color = 0xff000000 | Integer.reverseBytes((image.getColor(x,y)<<8) + 255);

                avR += (color >> 16) & 0xFF;
                avG += (color >> 8) & 0xFF;
                avB += color & 0xFF;
            }
        }
        int r = avR/samples; int g = avG/samples; int b = avB/samples;
        return new int[]{r,g,b};
    }

    private static final HashMap<Integer, ImageInfo> cache = new HashMap<>(1_000);

public static long time = 0;
    public static void changeToTexture(NativeImage img, int x, int y) {
        int[] averageColor = getAverageRGB(img,x,y);
        ImageInfo info = getBestImageInfo(averageColor);
        if (info == null) return;
        long millis = System.currentTimeMillis();
        ExampleMod.renderTexture(x,y,TEXTURE_SIZE,TEXTURE_SIZE,info.id());
        /*if (sampleImage == null) return;

        for (int i = 0; i < TEXTURE_SIZE; i++) {
            for (int j = 0; j < TEXTURE_SIZE; j++) {
                if (i+x >= img.getWidth() || j+y >= img.getHeight()) continue;

                img.setColor(i+x,j+y,Integer.reverseBytes((sampleImage.getRGB(i,j)<<8) + 255));

            }
        }*/
        //1st create shader that takes scaled down version of the frame buffer and replaces it by pointers to the block objects
        time += System.currentTimeMillis()-millis;
    }

    public static void resetCache() {
        cache.clear();
        sampleImages.update();
    }

    public static ImageInfo getBestImageInfo(int[] averageColor) {
        int color =((averageColor[0] & 0xFF) << 16) | ((averageColor[1] & 0xFF) << 8)  | ((averageColor[2] & 0xFF));
        if (cache.containsKey(color)) return cache.get(color);

        long lowestDiff = Long.MAX_VALUE;
        ImageInfo bestInfo = null;
        List<ImageInfo> imageInfos = sampleImages.getImageInfoList();
        for (ImageInfo info : imageInfos) {
            int[] infoAverageColor = info.averageColor();
            int r = Math.abs(averageColor[0] - infoAverageColor[0]);
            int g = Math.abs(averageColor[1] - infoAverageColor[1]);
            int b = Math.abs(averageColor[2] - infoAverageColor[2]);

            long diff = r + g + b;
            if (diff < lowestDiff) {
                lowestDiff = diff;
                bestInfo = info;
            }
        }


        cache.put(color,bestInfo);
        return bestInfo;
    }

}
