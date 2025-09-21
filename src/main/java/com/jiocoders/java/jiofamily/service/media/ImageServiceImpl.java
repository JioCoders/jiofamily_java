package com.jiocoders.java.jiofamily.service.media;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageServiceImpl implements ImageService {

    private final Path root = Paths.get("ims-uploads").toAbsolutePath().normalize();

    @Override
    public void init() {
        try {
            if (!Files.exists(root)) {
                Files.createDirectory(root);
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload!");
        }
    }

    @Override
    public String save(MultipartFile file, String empId) {
        // Normalize file name
        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            int dotIndex = originalFileName.lastIndexOf('.');
            String str = (dotIndex == -1) ? originalFileName : originalFileName.substring(0, dotIndex);
            String newFileName = originalFileName.replace(str, empId);

            // Check if the file's name contains invalid characters
            if (newFileName.contains("..")) {
                throw new RuntimeException("Sorry! Filename contains invalid path sequence " + newFileName);
            }

            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = this.root.resolve(newFileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return newFileName;
        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }

    @Override
    public Resource load(String filename) {
        try {
            Path file = root.resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(root.toFile());
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            Stream<Path> pathStream = Files.walk(this.root, 1).filter(path -> !path.equals(this.root))
                    .map(this.root::relativize);
            return pathStream;
        } catch (IOException e) {
            throw new RuntimeException("Could not load the files!");
        }
    }

    @Override
    public boolean imageFile(String fileName) {
        // Regex to check valid image file extension.
        String regex = "([^\\s]+(\\.(?i)(jpe?g|png))$)";

        Pattern p = Pattern.compile(regex);
        if (fileName == null) {
            return false;
        }
        Matcher m = p.matcher(fileName);
        return m.matches();
    }

    @Override
    public boolean checkJPEG(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename().toUpperCase();
        boolean extension = fileName.endsWith(".JPG") || fileName.endsWith(".JPEG") || fileName.endsWith(".PNG");
        if (!extension) {
            return false;
        }
        FileInputStream in = null;
        try {
            in = new FileInputStream(file.getName());
            byte[] magic = new byte[3];
            int count = in.read(magic);
            if (count < 3)
                return false;
            return magic[0] == 0xFF && magic[1] == 0xD8 && magic[2] == 0xFF;
        } finally {
            try {
                if (in != null)
                    in.close();
            } catch (IOException ex) {
            }
        }
    }
}