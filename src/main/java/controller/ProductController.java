package controller;

import dto.ProductRequest;
import dto.ProductResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/product")
@Slf4j
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping("")
    public ProductResponse addProduct(@RequestBody ProductRequest productDto) {
        log.info("Product creation request from the customer {}", productDto);
        return productService.createProduct(productDto);
    }

    @GetMapping("")
    public List<ProductResponse> getAllProducts() {
        log.info("Getting all products from the customer");
        return productService.getAllProducts();
    }

    @DeleteMapping("/{article}")
    public void deleteProduct(@PathVariable String article) {
        log.info("Deleting product {} from the customer", article);
        productService.deleteProduct(article);
    }

    @PutMapping("/{article}")
    public ProductResponse updateProduct(@RequestBody ProductRequest productDto,
                                @PathVariable String article) {
        log.info("Updating product {} from the customer", article);
        return productService.update(productDto, article);
    }

}
