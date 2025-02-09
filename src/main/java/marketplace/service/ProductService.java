package marketplace.service;

import marketplace.controller.response.OrderAndTinResponse;
import marketplace.controller.response.OrderResponse;
import marketplace.dto.SearchFilter;
import marketplace.controller.request.ProductRequestCreate;
import marketplace.controller.request.ProductRequestUpdate;
import marketplace.controller.response.ProductResponse;
import marketplace.entity.Product;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ProductService {

    ProductResponse createProduct(ProductRequestCreate productDto);
    ProductResponse updateProduct(ProductRequestUpdate productDto, Integer productArticle);
    UUID deleteProduct(Integer productArticle);
    ProductResponse getProduct(Integer productArticle);
    List<ProductResponse> getAllProducts(Integer pageNumber, Integer pageSize);
    List<ProductResponse> searchProducts(SearchFilter searchFilter);
    Map<Integer, List<OrderAndTinResponse>> getAllOrderForEveryProduct();
    List<OrderAndTinResponse> getOrdersForProduct(Integer productArticle);
    Product bookProduct(Integer productArticle, Integer quantity);
    void returnOfProductsToWarehouse(Map<Product, Integer> products);

}
