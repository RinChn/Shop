package marketplace.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marketplace.service.implementation.DocumentsServiceImpl;
import marketplace.util.FileHandler;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("api/v1/documents")
@Slf4j
@RequiredArgsConstructor
public class DocumentsController {

    private final DocumentsServiceImpl documentService;

    @GetMapping("")
    public List<String> getReports() {
        log.info("Request to get the names of all the reporters");
        return documentService.allFilesNames();
    }

    @PostMapping("")
    public String upload(@RequestParam("file") MultipartFile file) {
        log.info("Uploading file {}", file.getOriginalFilename());
        return documentService.uploadDocument(file);
    }

    @GetMapping("/{name}")
    public ResponseEntity<ByteArrayResource> download(@PathVariable("name") String filename) {
        log.info("Downloading file {}", filename);
        MultipartFile file = documentService.downloadDocument(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" +
                        file.getOriginalFilename() + "\"")
                .contentType(MediaType.parseMediaType(Objects.requireNonNull(file.getContentType())))
                .body(new ByteArrayResource(FileHandler.getFileContent(file)));
    }
}
