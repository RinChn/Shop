package marketplace.service;

import marketplace.dto.ProductResponse;
import marketplace.entity.Product;
import marketplace.exception.ApplicationException;
import marketplace.repository.ProductRepository;
import marketplace.util.TestProduct;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {
    @Mock
    ProductRepository productRepository;
    @Mock
    ConversionService conversionService;
    @InjectMocks
    ProductServiceImpl productService;


    @Test
    void getAllProducts() {
        Mockito.when(productRepository.findAll(PageRequest.of(0, 5)))
                .thenReturn(new PageImpl<>(List.of(TestProduct.dress, TestProduct.ball)));
        Mockito.when(conversionService.convert(TestProduct.dress, ProductResponse.class))
                .thenReturn(TestProduct.dressResponse);
        Mockito.when(conversionService.convert(TestProduct.ball, ProductResponse.class))
                .thenReturn(TestProduct.ballResponse);

        assertEquals(List.of(TestProduct.dressResponse, TestProduct.ballResponse),
                productService.getAllProducts(0, 5));
    }

    @Test
    void getProduct_successfully() {
        Mockito.when(productRepository.findByArticle(TestProduct.ball.getArticle()))
                .thenReturn(Optional.ofNullable(TestProduct.ball));
        Mockito.when(conversionService.convert(TestProduct.ball, ProductResponse.class))
                .thenReturn(TestProduct.ballResponse);
        assertEquals(TestProduct.ballResponse,
                productService.getProduct(TestProduct.ball.getArticle()));
    }

    @Test
    void getProduct_notFound() {
        Mockito.when(productRepository.findByArticle(TestProduct.ball.getArticle()))
                .thenReturn(Optional.empty());
        assertThrows(ApplicationException.class, () ->
            productService.getProduct(TestProduct.ball.getArticle())
        );
    }

    @Test
    void createProduct_successfully() {
        Mockito.when(productRepository.findByArticle(TestProduct.ball.getArticle()))
                .thenReturn(Optional.empty());
        Mockito.when(conversionService.convert(TestProduct.ball, ProductResponse.class))
                .thenReturn(TestProduct.ballResponse);
        Mockito.when(conversionService.convert(TestProduct.ballRequestCreate, Product.class))
                .thenReturn(TestProduct.ball);
        assertEquals(TestProduct.ballResponse,
                productService.createProduct(TestProduct.ballRequestCreate));
    }

    @Test
    void createProduct_duplicate() {
        Mockito.when(productRepository.findByArticle(TestProduct.ball.getArticle()))
                .thenReturn(Optional.ofNullable(TestProduct.ball));
        Mockito.when(conversionService.convert(TestProduct.ballRequestCreate, Product.class))
                .thenReturn(TestProduct.ball);
        assertThrows(ApplicationException.class, () ->
                productService.createProduct(TestProduct.ballRequestCreate));
    }

    @Test
    void deleteProduct_successfully() {
        Mockito.when(productRepository.findByArticle(TestProduct.ball.getArticle()))
                .thenReturn(Optional.ofNullable(TestProduct.ball));
        assertEquals(TestProduct.ball.getId(),
                productService.deleteProduct(TestProduct.ball.getArticle()));
    }

    @Test
    void deleteProduct_notFound() {
        Mockito.when(productRepository.findByArticle(TestProduct.ball.getArticle()))
                .thenReturn(Optional.empty());
        assertThrows(ApplicationException.class, () ->
                productService.deleteProduct(TestProduct.ball.getArticle()));
    }

    @Test
    void updateProduct_successfully() {
        Mockito.when(productRepository.findByArticle(TestProduct.ball.getArticle()))
                .thenReturn(Optional.ofNullable(TestProduct.ball));
        Mockito.when(conversionService.convert(TestProduct.ball, ProductResponse.class))
                .thenReturn(TestProduct.ballResponseAfterUpd);
        assertEquals(TestProduct.ballResponseAfterUpd,
                productService.updateProduct(TestProduct.ballRequestUpdate, TestProduct.ball.getArticle()));
    }

    @Test
    void updateProduct_notFound() {
        Mockito.when(productRepository.findByArticle(TestProduct.ball.getArticle()))
                .thenReturn(Optional.empty());
        assertThrows(ApplicationException.class, () ->
                productService.updateProduct(TestProduct.ballRequestUpdate, TestProduct.ball.getArticle()));
    }

}
