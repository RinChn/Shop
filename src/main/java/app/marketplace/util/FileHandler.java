package app.marketplace.util;

import app.marketplace.entity.Product;
import app.exception.ApplicationException;
import app.exception.ErrorType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

public class FileHandler {
    public static void AddingListOfProductsToExcel(List<Product> products) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Main sheet");
        fillSheet(sheet, products);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy_HH.mm");
        String dateForFileName = LocalDateTime.now()
                .truncatedTo(ChronoUnit.SECONDS)
                .format(formatter);
        String filePath = "src\\main\\resources\\reports\\report-" + dateForFileName + ".xlsx";
        saveWorkbookToFile(workbook, filePath);
    }

    private static void fillSheet(XSSFSheet sheet, List<Product> products) {
        List<String> headers = getProductFieldNames();
        createHeaderRow(sheet, headers);
        for (int i = 0; i < products.size(); i++) {
            Row row = sheet.createRow(i + 1);
            fillProductData(row, products.get(i));
        }
    }

    private static void createHeaderRow(XSSFSheet sheet, List<String> headers) {
        Row heads = sheet.createRow(0);
        for (int i = 1; i < headers.size(); i++) {
            heads.createCell(i - 1).setCellValue(headers.get(i));
        }
    }

    private static void fillProductData(Row row, Product product) {
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

    private static List<String> getProductFieldNames() {
        return Arrays.stream(Product.class.getDeclaredFields())
                .map(Field::getName)
                .toList();
    }

    private static void saveWorkbookToFile(XSSFWorkbook workbook, String filePath) {
        try (FileOutputStream out = new FileOutputStream(filePath)) {
            workbook.write(out);
        } catch (IOException exception) {
            throw new ApplicationException(ErrorType.ERROR_FILE_SAVED);
        }
    }

    public static byte[] getFileContent(MultipartFile file) {
        byte[] fileContent;
        try {
            fileContent = file.getBytes();
        } catch (IOException exception) {
            throw new ApplicationException(ErrorType.INVALID_FILE_CONTENT);
        }
        return fileContent;
    }
}
