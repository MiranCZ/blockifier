package net.fabricmc.example;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class FileHandler {

    public static List<Path> getPathsFromResourceJAR(String folder) throws URISyntaxException, IOException {
        List<Path> result;

        String jarPath = FileHandler.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();

        URI uri = URI.create("jar:file:" + jarPath);
        try (FileSystem fs = FileSystems.getFileSystem(uri)) {
            result = Files.walk(fs.getPath(folder))
                    .filter(Files::isRegularFile)
                    .collect(Collectors.toList());
        }

        return result;

    }

    public static InputStream getFileFromResourceAsStream(String fileName) {
        ClassLoader classLoader = FileHandler.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);

        if (inputStream == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            return inputStream;
        }

    }

}
