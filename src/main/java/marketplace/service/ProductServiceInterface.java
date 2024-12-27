package marketplace.service;

import marketplace.dto.ProductRequestCreate;
import marketplace.dto.ProductRequestUpdate;
import marketplace.dto.ProductResponse;

import java.util.List;

public interface ProductServiceInterface {

    public ProductResponse createProduct(ProductRequestCreate productDto);
    public ProductResponse updateProduct(ProductRequestUpdate productDto, Integer productArticle);
    public void deleteProduct(Integer productArticle);
    public ProductResponse getProduct(Integer productArticle);
    public List<ProductResponse> getAllProducts();

}
