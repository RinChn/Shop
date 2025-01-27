package marketplace.service;

import marketplace.dto.Filter;
import marketplace.dto.ProductRequestCreate;
import marketplace.dto.ProductRequestUpdate;
import marketplace.dto.ProductResponse;

import java.util.List;
import java.util.UUID;

public interface ProductService {

    ProductResponse createProduct(ProductRequestCreate productDto);
    ProductResponse updateProduct(ProductRequestUpdate productDto, Integer productArticle);
    UUID deleteProduct(Integer productArticle);
    ProductResponse getProduct(Integer productArticle);
    List<ProductResponse> getAllProducts(Integer pageNumber, Integer pageSize);
    List<ProductResponse> searchProducts(Filter filter);

}
