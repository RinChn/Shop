package marketplace.service;

import marketplace.aspect.Timer;
import marketplace.dto.Filter;
import marketplace.dto.ProductRequestUpdate;
import marketplace.dto.ProductRequestCreate;
import marketplace.dto.ProductResponse;
import marketplace.entity.Product;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import marketplace.exception.ApplicationException;
import marketplace.exception.ErrorType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import marketplace.repository.ProductRepository;
import org.springframework.transaction.annotation.Transactional;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Consumer;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ConversionService conversionService;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    @Timer
    public ProductResponse createProduct(ProductRequestCreate productDto) {
        Product product = conversionService.convert(productDto, Product.class);
        productRepository.findByArticle(product.getArticle())
                .ifPresent(resultCheckingProduct -> {
                    throw new ApplicationException(ErrorType.DUPLICATE);
                });
        productRepository.save(product);
        log.info("Created product with article {}", product.getArticle());
        return conversionService.convert(product, ProductResponse.class);
    }

    @Override
    @Transactional(readOnly = true)
    @Timer
    public List<ProductResponse> getAllProducts(Integer pageNumber, Integer pageSize) {
        return productRepository.findAll(PageRequest.of(pageNumber, pageSize))
                .stream()
                .map(product -> conversionService.convert(product, ProductResponse.class))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    @Timer
    public ProductResponse getProduct(Integer productArticle) {
        Product resultSearch = productRepository
                .findByArticle(productArticle)
                .orElseThrow(() -> new ApplicationException(ErrorType.NONEXISTENT_ARTICLE));
        return conversionService.convert(resultSearch, ProductResponse.class);
    }

    @Override
    @Transactional
    @Timer
    public UUID deleteProduct(Integer productArticle) {
        Product product = productRepository
                .findByArticle(productArticle)
                .orElseThrow(() -> new ApplicationException(ErrorType.NONEXISTENT_ARTICLE));
        productRepository.delete(product);
        log.info("Deleted product with article {}", productArticle);
        return product.getId();
    }

    @Override
    @Transactional
    @Timer
    public ProductResponse updateProduct(ProductRequestUpdate request, Integer productArticle) {
        Product product = productRepository
                .findByArticle(productArticle)
                .orElseThrow(() -> new ApplicationException(ErrorType.NONEXISTENT_ARTICLE));
        setAllFieldsIfNotNull(product, request);
        productRepository.save(product);
        log.info("Updated product with article {}", product.getArticle());
        return conversionService.convert(product, ProductResponse.class);
    }

    private void setAllFieldsIfNotNull(Product product, ProductRequestUpdate request) {
        setFieldIfNotNull(request.getName(), product::setName);
        setFieldIfNotNull(request.getDescription(), product::setDescription);
        setFieldIfNotNull(request.getPrice(), product::setPrice);
        setFieldIfNotNull(request.getCategories(), product::setCategories);
        setFieldIfNotNull(request.getQuantity(), product::setQuantity);
        setFieldIfNotNull(request.getIsAvailable(), product::setIsAvailable);
    }

    private <T> void setFieldIfNotNull(T value, Consumer<T> setter) {
        Optional.ofNullable(value).ifPresent(setter);
    }

    @Override
    @Timer
    @Transactional(readOnly = true)
    public List<ProductResponse> searchProducts(Filter filter) {
        List<Product> foundProducts = productRepository.searchUsingFilter(filter.getName(), filter.getQuantity(),
                filter.getPrice(), filter.getIsAvailable());
        fillXlsxFile(foundProducts);
        return foundProducts.stream()
                .map(product -> conversionService.convert(product, ProductResponse.class))
                .toList();
    }

    public void fillXlsxFile(List<Product> products) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Main sheet");
        fillSheet(sheet, products);
        String dateForFileName = LocalDateTime.now()
                .truncatedTo(ChronoUnit.SECONDS)
                .toString().replace(":", "-").replace("T", "_");;
        String filePath = "src\\main\\resources\\reports\\report-" + dateForFileName + ".xlsx";
        saveWorkbookToFile(workbook, filePath);
    }

    private void fillSheet(XSSFSheet sheet, List<Product> products) {
        List<String> headers = getProductFieldNames();
        createHeaderRow(sheet, headers);
        for (int i = 0; i < products.size(); i++) {
            Row row = sheet.createRow(i + 1);
            fillProductData(row, products.get(i));
        }
    }

    private void createHeaderRow(XSSFSheet sheet, List<String> headers) {
        Row heads = sheet.createRow(0);
        for (int i = 1; i < headers.size(); i++) {
            heads.createCell(i - 1).setCellValue(headers.get(i));
        }
    }

    private void fillProductData(Row row, Product product) {
        row.createCell(0).setCellValue(product.getArticle());
        row.createCell(1).setCellValue(product.getName());
        row.createCell(2).setCellValue(product.getDescription());
        row.createCell(3).setCellValue(product.getCategories().toString());
        row.createCell(4).setCellValue(product.getPrice().toString());
        row.createCell(5).setCellValue(product.getQuantity().toString());
        row.createCell(6).setCellValue(product.getDateOfLastChangesQuantity().toString());
        row.createCell(7).setCellValue(product.getDateOfCreation().toString());
        row.createCell(8).setCellValue(product.getIsAvailable().toString());
    }

    private List<String> getProductFieldNames() {
        return Arrays.stream(Product.class.getDeclaredFields())
                .map(Field::getName)
                .toList();
    }

    private void saveWorkbookToFile(XSSFWorkbook workbook, String filePath) {
        try (FileOutputStream out = new FileOutputStream(filePath)) {
            workbook.write(out);
        } catch (IOException exception) {
            throw new RuntimeException("Failed to save workbook to file", exception);
        }
    }

}
