package pl.nullpointerexception.shop.admin.product.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class ExistingFileRenameUtilsTest {

    @Test
    void should_not_rename_file(@TempDir Path tempDir) {
        String newName = ExistingFileRenameUtils.renameIfExists(tempDir, "test.png");
        assertEquals("test.png", newName);
    }

    @Test
    void should_rename_existing_file(@TempDir Path tempDir) throws IOException {
        Files.createFile(tempDir.resolve("test.png"));
        String newName = ExistingFileRenameUtils.renameIfExists(tempDir, "test.png");
        assertEquals("test-1.png", newName);
    }

    @Test
    void should_rename_many_existing_file(@TempDir Path tempDir) throws IOException {
        Files.createFile(tempDir.resolve("test.png"));
        Files.createFile(tempDir.resolve("test-1.png"));
        Files.createFile(tempDir.resolve("test-2.png"));
        Files.createFile(tempDir.resolve("test-3.png"));

        String newName = ExistingFileRenameUtils.renameIfExists(tempDir, "test.png");
        assertEquals("test-4.png", newName);
    }
}