package controller;

import dto.ProductRequest;
import dto.ProductResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/product")
@Slf4j
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/add")
    public String addProduct(@RequestBody ProductRequest productDto) {
        log.info("Product creation request from the customer {}", productDto);
        return productService.createProduct(productDto);
    }

    @GetMapping("")
    public List<ProductResponse> getAllProducts() {
        log.info("Getting all products from the customer");
        return productService.getAllProducts();
    }

    @DeleteMapping("/delete/{article}")
    public String deleteProduct(@PathVariable String article) {
        log.info("Deleting product {} from the customer", article);
        return productService.deleteProduct(article);
    }

    @PutMapping("/update/{article}/name/{newName}")
    public String updateProduct(@PathVariable("article") String productUUID,
                                @PathVariable("newName") String newName) {
        log.info("Updating product {} from the customer", productUUID);
        return productService.updateName(productUUID, newName);
    }

}
