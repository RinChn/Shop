package marketplace.service;

import marketplace.dto.ProductRequestCreate;
import marketplace.dto.ProductRequestUpdate;
import marketplace.dto.ProductResponse;

import java.util.List;
import java.util.UUID;

public interface ProductService {

    public ProductResponse createProduct(ProductRequestCreate productDto);
    public ProductResponse updateProduct(ProductRequestUpdate productDto, Integer productArticle);
    public UUID deleteProduct(Integer productArticle);
    public ProductResponse getProduct(Integer productArticle);
    public List<ProductResponse> getAllProducts(Integer pageNumber, Integer pageSize);

}
