package app.controller;

import app.dto.ProductRequest;
import app.dto.ProductResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import app.service.ProductService;

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
        return productService.createProduct(productDto);
    }

    @GetMapping("")
    public List<ProductResponse> getAllProducts() {
        return productService.getAllProducts();
    }

    @DeleteMapping("/delete/{article}")
    public String deleteProduct(@PathVariable String article) {
        return productService.deleteProduct(article);
    }

    @PutMapping("/update/{article}/name/{newName}")
    public String updateProduct(@PathVariable("article") String productUUID,
                                @PathVariable("newName") String newName) {
        return productService.updateName(productUUID, newName);
    }

}
