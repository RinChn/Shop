package marketplace.util;

import marketplace.dto.ProductRequestCreate;
import marketplace.dto.ProductRequestUpdate;

import java.math.BigDecimal;

public class IncorrectTestProduct {
    public static ProductRequestCreate dressCreateEmptyArticle = ProductRequestCreate.builder()
            .article(null)
            .name("Dress")
            .price(BigDecimal.valueOf(100000))
            .quantity(1)
            .build();

    public static ProductRequestCreate dressCreateNegativeArticle = ProductRequestCreate.builder()
            .article(-1)
            .name("Dress")
            .price(BigDecimal.valueOf(100000))
            .quantity(1)
            .build();

    public static ProductRequestCreate dressCreateEmptyName = ProductRequestCreate.builder()
            .article(1)
            .name(" ")
            .price(BigDecimal.valueOf(100000))
            .quantity(1)
            .build();

    public static ProductRequestCreate dressCreateEmptyPrice = ProductRequestCreate.builder()
            .article(1)
            .name("Dress")
            .price(null)
            .quantity(1)
            .build();

    public static ProductRequestCreate dressCreateNegativePrice = ProductRequestCreate.builder()
            .article(1)
            .name("Dress")
            .price(BigDecimal.valueOf(-100000))
            .quantity(1)
            .build();

    public static ProductRequestCreate dressCreateNegativeQuantity = ProductRequestCreate.builder()
            .article(1)
            .name("Dress")
            .price(BigDecimal.valueOf(100000))
            .quantity(-1)
            .build();

    public static ProductRequestUpdate dressUpdateNegativePrice = ProductRequestUpdate.builder()
            .price(BigDecimal.valueOf(-100000))
            .build();

    public static ProductRequestUpdate dressUpdateNegativeQuantity = ProductRequestUpdate.builder()
            .quantity(-1)
            .build();

}
