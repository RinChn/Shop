package marketplace.service;

import marketplace.dto.DocumentResponse;
import marketplace.entity.Document;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface DocumentsService {
    List<String> allFilesNamesFromFolderReport();
    List<DocumentResponse> getAllDocuments();
    String uploadDocument(MultipartFile file);
    Document downloadDocument(String documentName);
    UUID deleteDocument(String documentName);
}
