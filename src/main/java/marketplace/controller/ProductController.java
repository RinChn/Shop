package marketplace.controller;

import marketplace.dto.ProductRequestUpdate;
import marketplace.dto.ProductRequestCreate;
import marketplace.dto.ProductResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import marketplace.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("api/v1/product")
@Slf4j
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping("")
    public ProductResponse addProduct(@RequestBody ProductRequestCreate request) {
        log.info("Product creation request from the customer {}", request);
        return productService.createProduct(request);
    }

    @GetMapping("")
    public List<ProductResponse> getAllProducts() {
        log.info("Getting all products from the customer");
        return productService.getAllProducts();
    }

    @GetMapping("/{article}")
    public ProductResponse getProductByArticle(@PathVariable Integer article) {
        log.info("Getting product by article {}", article);
        return productService.getProduct(article);
    }

    @DeleteMapping("/{article}")
    public void deleteProduct(@PathVariable Integer article) {
        log.info("Deleting product {} from the customer", article);
        productService.deleteProduct(article);
    }

    @PutMapping("/{article}")
    public ProductResponse updateProduct(@RequestBody ProductRequestUpdate request,
                                @PathVariable Integer article) {
        log.info("Updating product {} from the customer", article);
        return productService.updateProduct(request, article);
    }

}
