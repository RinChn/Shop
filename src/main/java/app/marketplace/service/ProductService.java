package app.marketplace.service;

import app.marketplace.dto.Filter;
import app.marketplace.dto.ProductRequestCreate;
import app.marketplace.dto.ProductRequestUpdate;
import app.marketplace.dto.ProductResponse;

import java.util.List;
import java.util.UUID;

public interface ProductService {

    public ProductResponse createProduct(ProductRequestCreate productDto);
    public ProductResponse updateProduct(ProductRequestUpdate productDto, Integer productArticle);
    public UUID deleteProduct(Integer productArticle);
    public ProductResponse getProduct(Integer productArticle);
    public List<ProductResponse> getAllProducts(Integer pageNumber, Integer pageSize);
    public List<ProductResponse> searchProducts(Filter filter);

}
