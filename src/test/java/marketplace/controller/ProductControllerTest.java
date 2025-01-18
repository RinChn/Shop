package marketplace.controller;

import app.marketplace.repository.ProductRepository;
import marketplace.util.IncorrectTestProduct;
import marketplace.util.TestProduct;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.Optional;

import static io.restassured.RestAssured.given;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductControllerTest {

    @MockitoBean
    ProductRepository productRepository;

    @LocalServerPort
    private int port;

    @Test
    void getAllProducts() {
        when(productRepository.findAll(PageRequest.of(0, 5)))
                .thenReturn(new PageImpl<>(List.of(TestProduct.dress, TestProduct.ball)));
        given()
                .port(port)
                .when()
                .get("/api/v1/product")
                .then().log().body().statusCode(HttpStatus.OK.value());
    }

    @Test
    void getProductByArticle_successfully() {
        when(productRepository.findByArticle(TestProduct.ball.getArticle()))
                .thenReturn(Optional.ofNullable(TestProduct.ball));
        given().port(port)
                .when()
                .get("/api/v1/product/" + TestProduct.ball.getArticle())
                .then().log().body().statusCode(HttpStatus.OK.value());
    }

    @Test
    void getProductByArticle_notFound() {
        when(productRepository.findByArticle(TestProduct.ball.getArticle()))
            .thenReturn(Optional.empty());
        given().port(port)
                .when()
                .get("/api/v1/product/" + TestProduct.ball.getArticle())
                .then().log().body().statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void addProduct_successfully() {
        when(productRepository.findByArticle(TestProduct.ball.getArticle()))
                .thenReturn(Optional.empty());
        given().port(port)
                .contentType("application/json").body(TestProduct.ballRequestCreate)
                .when().post("/api/v1/product")
                .then().log().body()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void addProduct_duplicate() {
        when(productRepository.findByArticle(TestProduct.ball.getArticle()))
                .thenReturn(Optional.ofNullable(TestProduct.ball));
        given().port(port)
                .contentType("application/json").body(TestProduct.ballRequestCreate)
                .when().post("/api/v1/product")
                .then().log().body()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void addProduct_emptyArticle() {
        when(productRepository.findByArticle(TestProduct.dress.getArticle())).thenReturn(Optional.empty());
        given().port(port)
                .contentType("application/json").body(IncorrectTestProduct.dressCreateEmptyArticle)
                .when().post("/api/v1/product")
                .then().log().body()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void addProduct_negativeArticle() {
        when(productRepository.findByArticle(TestProduct.dress.getArticle())).thenReturn(Optional.empty());
        given().port(port)
                .contentType("application/json").body(IncorrectTestProduct.dressCreateNegativeArticle)
                .when().post("/api/v1/product")
                .then().log().body()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void addProduct_emptyName() {
        when(productRepository.findByArticle(TestProduct.dress.getArticle())).thenReturn(Optional.empty());
        given().port(port)
                .contentType("application/json").body(IncorrectTestProduct.dressCreateEmptyName)
                .when().post("/api/v1/product")
                .then().log().body()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void addProduct_emptyPrice() {
        when(productRepository.findByArticle(TestProduct.dress.getArticle())).thenReturn(Optional.empty());
        given().port(port)
                .contentType("application/json").body(IncorrectTestProduct.dressCreateEmptyPrice)
                .when().post("/api/v1/product")
                .then().log().body()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void addProduct_negativePrice() {
        when(productRepository.findByArticle(TestProduct.dress.getArticle())).thenReturn(Optional.empty());
        given().port(port)
                .contentType("application/json").body(IncorrectTestProduct.dressCreateNegativePrice)
                .when().post("/api/v1/product")
                .then().log().body()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void addProduct_negativeQuantity() {
        when(productRepository.findByArticle(TestProduct.dress.getArticle())).thenReturn(Optional.empty());
        given().port(port)
                .contentType("application/json").body(IncorrectTestProduct.dressCreateNegativeQuantity)
                .when().post("/api/v1/product")
                .then().log().body()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void deleteProduct_successfully() {
        when(productRepository.findByArticle(TestProduct.ball.getArticle()))
                .thenReturn(Optional.ofNullable(TestProduct.ball));
        given().port(port)
                .when().delete("/api/v1/product/" + TestProduct.ball.getArticle())
                .then().log().body()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void deleteProduct_notFound() {
        when(productRepository.findByArticle(TestProduct.ball.getArticle()))
                .thenReturn(Optional.empty());
        given().port(port)
                .when().delete("/api/v1/product/" + TestProduct.ball.getArticle())
                .then().log().body()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void updateProduct_successfully() {
        when(productRepository.findByArticle(TestProduct.ball.getArticle()))
                .thenReturn(Optional.ofNullable(TestProduct.ball));
        given().port(port)
                .contentType("application/json").body(TestProduct.ballRequestUpdate)
                .when().put("/api/v1/product/" + TestProduct.ball.getArticle())
                .then().log().body()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void updateProduct_notFound() {
        when(productRepository.findByArticle(TestProduct.ball.getArticle())).thenReturn(Optional.empty());
        given().port(port)
                .contentType("application/json").body(TestProduct.ballRequestUpdate)
                .when().put("/api/v1/product/" + TestProduct.ball.getArticle())
                .then().log().body()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void updateProduct_negativePrice() {
        when(productRepository.findByArticle(TestProduct.ball.getArticle()))
                .thenReturn(Optional.ofNullable(TestProduct.ball));
        given().port(port)
                .contentType("application/json").body(IncorrectTestProduct.dressUpdateNegativePrice)
                .when().put("/api/v1/product/" + TestProduct.ball.getArticle())
                .then().log().body()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void updateProduct_negativeQuantity() {
        when(productRepository.findByArticle(TestProduct.ball.getArticle()))
                .thenReturn(Optional.ofNullable(TestProduct.ball));
        given().port(port)
                .contentType("application/json").body(IncorrectTestProduct.dressUpdateNegativeQuantity)
                .when().put("/api/v1/product/" + TestProduct.ball.getArticle())
                .then().log().body()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }
}
