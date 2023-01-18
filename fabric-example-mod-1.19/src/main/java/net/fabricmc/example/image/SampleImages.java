package net.fabricmc.example.image;

import net.fabricmc.example.ExampleMod;
import net.minecraft.client.gl.PostEffectPass;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.util.Identifier;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static net.fabricmc.example.ExampleMod.TEXTURE_SIZE;
import static net.fabricmc.example.FileHandler.getFileFromResourceAsStream;
import static net.fabricmc.example.FileHandler.getPathsFromResourceJAR;

public class SampleImages {

    private final ArrayList<ImageInfo> imageInfos = new ArrayList<>();
    private final List<Path> result;


    public SampleImages() {
        List<Path> list = new ArrayList<>();
        try {
            //for JAR
            list = getPathsFromResourceJAR("block_textures");
        } catch (Exception e) {
            //for source
            File file = new File(getClass().getClassLoader().getResource("block_textures").getPath());

            for (File f : file.listFiles()) {
                list.add(Path.of(f.getAbsolutePath()));
            }
        }

        result = list;

        try {
            makeList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<ImageInfo> getImageInfoList() {
        return Collections.unmodifiableList(imageInfos);
    }

    public void update() {
     /*   imageInfos.clear();
        try {
            makeList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/
    }


    private void makeList() throws IOException {
        for (Path path : result) {
            String pathInJAR = path.toString();
            BufferedImage img = null;
            try {//JAR
                InputStream is = getFileFromResourceAsStream(pathInJAR);
                img = ImageIO.read(is);
            } catch (IllegalArgumentException e) {//source
                img = ImageIO.read(path.toFile());
            }


            if (img.getWidth() != img.getHeight()) continue;

            if (img.getWidth() != TEXTURE_SIZE) img = ImageHelper.changeRes(img, TEXTURE_SIZE, TEXTURE_SIZE);
            imageInfos.add(new ImageInfo(new NativeImageBackedTexture(bufferedToNativeImage(img)), ImageHelper.getAverageRGB(img), new Identifier("blockifier", imageInfos.size() + "")));
        }

    }

    private static NativeImage bufferedToNativeImage(BufferedImage sampleImage) {
        NativeImage img = new NativeImage(sampleImage.getWidth(),sampleImage.getHeight(),false);

        for (int i = 0; i < img.getWidth(); i++) {
            for (int j = 0; j < img.getHeight(); j++) {
                img.setColor(i,j,Integer.reverseBytes((sampleImage.getRGB(i,j)<<8) + 255));

            }
        }

        return img;
    }

}
