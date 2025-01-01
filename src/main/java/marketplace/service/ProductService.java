package marketplace.service;

import marketplace.dto.ProductRequestCreate;
import marketplace.dto.ProductRequestUpdate;
import marketplace.dto.ProductResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {

    public ProductResponse createProduct(ProductRequestCreate productDto);
    public ProductResponse updateProduct(ProductRequestUpdate productDto, Integer productArticle);
    public void deleteProduct(Integer productArticle);
    public ProductResponse getProduct(Integer productArticle);
    public List<ProductResponse> getAllProducts(Integer pageNumber, Integer pageSize);

}
