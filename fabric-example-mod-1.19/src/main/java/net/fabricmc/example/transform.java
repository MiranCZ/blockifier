package net.fabricmc.example;

import com.mojang.blaze3d.platform.GlStateManager;
import net.fabricmc.example.image.ImageHelper;
import net.fabricmc.example.image.ImageInfo;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.GlProgramManager;
import net.minecraft.client.gl.SimpleFramebuffer;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.util.Identifier;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static net.fabricmc.example.ExampleMod.TEXTURE_SIZE;
import static net.fabricmc.example.FileHandler.getFileFromResourceAsStream;
import static net.fabricmc.example.FileHandler.getPathsFromResourceJAR;

public class transform {
    private static List<Path> result;

    static {
        List<Path> list = new ArrayList<>();
        try {
            //for JAR
            list = getPathsFromResourceJAR("block_textures");
        } catch (Exception e) {
            //for source
            File file = new File(transform.class.getClassLoader().getResource("block_textures").getPath());

            for (File f : file.listFiles()) {
                list.add(Path.of(f.getAbsolutePath()));
            }
        }

        result = list;
    }

    public static void main(String[] args) throws IOException {
        BufferedImage resultImg = new BufferedImage(453*2,2,BufferedImage.TYPE_INT_RGB);
        Graphics2D g = resultImg.createGraphics();

        int i = 0;
        for (Path path : result) {
            String pathInJAR = path.toString();
            BufferedImage img = null;
            try {//JAR
                InputStream is = getFileFromResourceAsStream(pathInJAR);
                img = ImageIO.read(is);
            } catch (IllegalArgumentException e) {//source
                img = ImageIO.read(path.toFile());
            }
            BufferedImage smallerImg = ImageHelper.changeRes(img,2,2);
            g.drawImage(smallerImg,null,i,0);

            i+=2;
        }

        g.dispose();

        ImageIO.write(resultImg, "PNG",new File("blocks_2.png"));
    }


}
