package service;

import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    private final ConversionService conversionService;

    public ProductService(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

}
