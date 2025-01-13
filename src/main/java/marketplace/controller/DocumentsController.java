package marketplace.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marketplace.dto.DocumentResponse;
import marketplace.entity.Document;
import marketplace.service.DocumentsServiceImpl;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/documents")
@Slf4j
@RequiredArgsConstructor
public class DocumentsController {

    private final DocumentsServiceImpl documentService;

    @GetMapping("/local")
    public List<String> getLocalReports() {
        log.info("Request to receive reports from a local folder");
        return documentService.allFilesNamesFromFolderReport();
    }

    @GetMapping("/global")
    public List<DocumentResponse> getAllDocuments() {
        log.info("Request for documents from the database");
        return documentService.getAllDocuments();
    }

    @PostMapping("")
    public String uploadDocument(@RequestParam("file") MultipartFile file) {
        log.info("Request to save a new document with a name: {}", file.getOriginalFilename());
        return documentService.uploadDocument(file);
    }

    @GetMapping("/{name}")
    public ResponseEntity<ByteArrayResource> downloadDocument(@PathVariable("name") String fileName) {
        log.info("Request to download a document with a name: {}", fileName);
        Document document = documentService.downloadDocument(fileName);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + document.getName() + "\"")
                .contentType(MediaType.parseMediaType(document.getMimeType()))
                .body(new ByteArrayResource(document.getContent()));
    }

    @DeleteMapping("/{name}")
    public UUID deleteDocument(@PathVariable("name") String fileName) {
        log.info("Request to delete a document with a name: {}", fileName);
        return documentService.deleteDocument(fileName);
    }
}
