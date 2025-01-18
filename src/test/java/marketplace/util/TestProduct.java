package marketplace.util;

import app.marketplace.dto.ProductRequestCreate;
import app.marketplace.dto.ProductRequestUpdate;
import app.marketplace.dto.ProductResponse;
import app.marketplace.entity.Category;
import app.marketplace.entity.Product;

import java.math.BigDecimal;

public class TestProduct {

    public static Product dress = Product.builder()
            .article(1)
            .name("Dress")
            .categories(Category.CLOTHES)
            .description("Nice dress")
            .price(BigDecimal.valueOf(100000))
            .quantity(1)
            .build();
    public static ProductResponse dressResponse = ProductResponse.builder()
            .article(1)
            .name("Dress")
            .categories(Category.CLOTHES)
            .description("Nice dress")
            .price(BigDecimal.valueOf(100000))
            .quantity(1)
            .build();

    public static Product ball = Product.builder()
            .article(2)
            .name("Ball")
            .categories(Category.SPORTS)
            .description("Big ball")
            .price(BigDecimal.valueOf(150))
            .quantity(1000)
            .build();
    public static ProductResponse ballResponseAfterUpd = ProductResponse.builder()
            .article(2)
            .name("Ball UPD")
            .categories(Category.SPORTS)
            .description("Big ball")
            .price(BigDecimal.valueOf(150))
            .quantity(1000)
            .build();
    public static ProductResponse ballResponse = ProductResponse.builder()
            .article(2)
            .name("Ball")
            .categories(Category.SPORTS)
            .description("Big ball")
            .price(BigDecimal.valueOf(150))
            .quantity(1000)
            .build();
    public static ProductRequestCreate ballRequestCreate = ProductRequestCreate.builder()
            .article(2)
            .name("Ball")
            .categories(Category.SPORTS)
            .description("Big ball")
            .price(BigDecimal.valueOf(150))
            .quantity(1000)
            .build();
    public static ProductRequestUpdate ballRequestUpdate = ProductRequestUpdate.builder()
            .name("Ball UPD")
            .build();

}
