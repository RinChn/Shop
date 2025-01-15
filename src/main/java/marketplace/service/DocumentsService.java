package marketplace.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

public interface DocumentsService {
    List<String> allFilesNames();
    String uploadDocument(MultipartFile multipartFile);
    MultipartFile downloadDocument(String fileName);
}
