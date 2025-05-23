package marketplace.controller;

import jakarta.validation.Valid;
import marketplace.controller.response.OrderAndTinResponse;
import marketplace.controller.response.OrderResponse;
import marketplace.dto.SearchFilter;
import marketplace.controller.request.ProductRequestUpdate;
import marketplace.controller.request.ProductRequestCreate;
import marketplace.controller.response.ProductResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import marketplace.service.ProductServiceImpl;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/product")
@Slf4j
@RequiredArgsConstructor
public class ProductController {

    private final ProductServiceImpl productService;

    @PostMapping("")
    public ProductResponse addProduct(@Valid @RequestBody ProductRequestCreate request) {
        log.info("Sending a request from the controller to create an object with the article {}",
                request.getArticle());
        return productService.createProduct(request);
    }

    @GetMapping("")
    public List<ProductResponse> getAllProducts(@RequestParam(value = "page", defaultValue = "0") Integer pageNumber,
                                                @RequestParam(value = "limit", defaultValue = "5") Integer pageSize) {
        log.info("Requesting all products from the controller");
        return productService.getAllProducts(pageNumber, pageSize);
    }

    @GetMapping("/{article}")
    public ProductResponse getProductByArticle(@PathVariable Integer article) {
        log.info("Request from the controller to receive an item with the article number {}", article);
        return productService.getProduct(article);
    }

    @GetMapping("/orders")
    public Map<Integer, List<OrderAndTinResponse>> getAllOrderForEveryProduct() {
        return productService.getAllOrderForEveryProduct();
    }

    @GetMapping("/orders/{article}")
    public List<OrderAndTinResponse> getOrderForProduct(@PathVariable Integer article) {
        return productService.getOrdersForProduct(article);
    }

    @DeleteMapping("/{article}")
    public UUID deleteProduct(@PathVariable Integer article) {
        log.info("Request from the controller to delete the item with the article number {}", article);
        return productService.deleteProduct(article);
    }

    @PutMapping("/{article}")
    public ProductResponse updateProduct(@Valid @RequestBody ProductRequestUpdate request,
                                @PathVariable Integer article) {
        log.info("Request from the controller to update the item with the article number {}", article);
        return productService.updateProduct(request, article);
    }

    @GetMapping("/search")
    public List<ProductResponse> searchProducts(@RequestBody SearchFilter searchFilter) {
        log.info("A request from the controller to search for a product with the values: name = {}, price = {},\n" +
                        "quantity = {}, isAvailable = {}",
        searchFilter.getName(), searchFilter.getPrice(), searchFilter.getQuantity(), searchFilter.getIsAvailable());
        return productService.searchProducts(searchFilter);
    }

}
