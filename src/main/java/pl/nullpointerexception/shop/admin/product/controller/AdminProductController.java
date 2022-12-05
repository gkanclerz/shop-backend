package pl.nullpointerexception.shop.admin.product.controller;

import com.github.slugify.Slugify;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.nullpointerexception.shop.admin.product.dto.AdminProductDto;
import pl.nullpointerexception.shop.admin.product.dto.UploadResponse;
import pl.nullpointerexception.shop.admin.product.model.AdminProduct;
import pl.nullpointerexception.shop.admin.product.service.AdminProductImageService;
import pl.nullpointerexception.shop.admin.product.service.AdminProductService;

import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
@RequiredArgsConstructor
public class AdminProductController {

    public static final Long EMPTY_ID = null;
    private final AdminProductService productService;
    private final AdminProductImageService productImageService;

    @GetMapping("/admin/products")
    public Page<AdminProduct> getProducts(Pageable pageable) {
        return productService.getProducts(pageable);
    }

    @GetMapping("/admin/products/{id}")
    public AdminProduct getProduct(@PathVariable Long id) {
        return productService.getProduct(id);
    }

    @PostMapping("/admin/products")
    public AdminProduct createProduct(@RequestBody @Valid AdminProductDto adminProductDto) {
        return productService.createProduct(mapAdminProduct(adminProductDto, EMPTY_ID));
    }

    @PutMapping("/admin/products/{id}")
    public AdminProduct updateProduct(@RequestBody @Valid AdminProductDto adminProductDto, @PathVariable Long id) {
        return productService.updateProduct(mapAdminProduct(adminProductDto, id));
    }

    @DeleteMapping("/admin/products/{id}")
    public void deleteProduct(@PathVariable Long id){
        productService.deleteProduct(id);
    }

    @PostMapping("/admin/products/upload-image/")
    public UploadResponse uploadImage(@RequestParam("file") MultipartFile multipartFile){
        String filename = multipartFile.getOriginalFilename();

        try(InputStream inputStream =multipartFile.getInputStream()) {
            String savedFilename = productImageService.uploadImage(filename, inputStream);
            return new UploadResponse(savedFilename);
        } catch (IOException e) {
            throw  new RuntimeException("Coś poszło źle podczas wgrywania pliku", e);
        }
    }

    @GetMapping("data/productImage/{filename}")
    public ResponseEntity<Resource> serveFiles(@PathVariable String filename) throws IOException {
        Resource file = productImageService.serveFiles(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, Files.probeContentType(Path.of(filename)))
                .body(file);
    }

    private AdminProduct mapAdminProduct(AdminProductDto adminProductDto, Long id) {
        return AdminProduct.builder()
                .id(id)
                .name(adminProductDto.getName())
                .description(adminProductDto.getDescription())
                .category(adminProductDto.getCategory())
                .price(adminProductDto.getPrice())
                .currency(adminProductDto.getCurrency())
                .image(adminProductDto.getImage())
                .slug(slugifySlug(adminProductDto.getSlug()))
                .build();
    }

    private String slugifySlug(String slug) {
        Slugify slugify = new Slugify();
        return slugify.withCustomReplacement("_","-")
                .slugify(slug);
    }
}
