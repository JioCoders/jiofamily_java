package com.jiocoders.java.jiofamily.service.media;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    void init();

    String save(MultipartFile file, String empId);

    Resource load(String filename);

    void deleteAll();

    Stream<Path> loadAll();

    boolean imageFile(String fileName);

    boolean checkJPEG(MultipartFile file) throws IOException;
}