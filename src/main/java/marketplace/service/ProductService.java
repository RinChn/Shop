package marketplace.service;

import marketplace.dto.SearchFilter;
import marketplace.controller.request.ProductRequestCreate;
import marketplace.controller.request.ProductRequestUpdate;
import marketplace.controller.response.ProductResponse;

import java.util.List;
import java.util.UUID;

public interface ProductService {

    ProductResponse createProduct(ProductRequestCreate productDto);
    ProductResponse updateProduct(ProductRequestUpdate productDto, Integer productArticle);
    UUID deleteProduct(Integer productArticle);
    ProductResponse getProduct(Integer productArticle);
    List<ProductResponse> getAllProducts(Integer pageNumber, Integer pageSize);
    List<ProductResponse> searchProducts(SearchFilter searchFilter);

}
