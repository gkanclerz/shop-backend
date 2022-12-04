package pl.nullpointerexception.shop.admin.product.service;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class UploadedFilesNameUtilsTest {

    @ParameterizedTest
    @CsvSource({
            "test test.png, test-test.png",
            "hello world.png, hello-world.png",
            "ąęśćżźńłó.png, aesczznlo.png",
            "Produkt 1.png, produkt-1.png",
            "Produkt - 1.png, produkt-1.png",
            "Produkt   1.png, produkt-1.png",
            "Produkt__1.png, produkt-1.png",

    })
    void should_slugify_fileName(String in, String out){
        String fileName = UploadedFilesNameUtils.slugifyFileName(in);
        assertEquals(fileName,out);
    }
}