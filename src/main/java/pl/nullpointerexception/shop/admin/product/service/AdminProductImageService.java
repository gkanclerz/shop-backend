package pl.nullpointerexception.shop.admin.product.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class AdminProductImageService {
    @Value("${app.uploadDir}")
    private String uploadDir = "";

    public String uploadImage(String filename, InputStream inputStream){
        String newFilename = UploadedFilesNameUtils.slugifyFileName(filename);
        newFilename = ExistingFileRenameUtils.renameIfExists(Path.of(uploadDir), newFilename);
        Path filePath = Paths.get(uploadDir).resolve(newFilename);

        try (OutputStream outputStream = Files.newOutputStream(filePath);){
            inputStream.transferTo(outputStream);
        } catch (IOException e) {
            throw new RuntimeException("Nie moge zapisac pliku,", e);
        }
        return newFilename;
    }


    public Resource serveFiles(String filename) {
        FileSystemResourceLoader fileSystemResourceLoader = new FileSystemResourceLoader();
        return fileSystemResourceLoader.getResource(uploadDir + filename);
    }
}
