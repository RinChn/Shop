package marketplace.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marketplace.dto.DocumentResponse;
import marketplace.exception.ApplicationException;
import marketplace.exception.ErrorType;
import marketplace.repository.DocumentRepository;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import marketplace.entity.Document;

@Service
@Slf4j
@RequiredArgsConstructor
public class DocumentsServiceImpl implements DocumentsService {

    private final ConversionService conversionService;
    private final DocumentRepository documentRepository;

    @Override
    public List<String> allFilesNamesFromFolderReport() {
        File[] allFiles = new File("src/main/resources/reports").listFiles();
        List<String> fileNames = new ArrayList<>();
        for (File file : allFiles) {
            fileNames.add(file.getName());
        }
        log.info("Returning all file names from folder");
        return fileNames;
    }

    @Override
    public List<DocumentResponse> getAllDocuments() {
        log.info("Returning all documents from db");
        return documentRepository.findAll()
                .stream()
                .map(document -> conversionService.convert(document, DocumentResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public String uploadDocument(MultipartFile file) {
        Document document;
        try {
            document = Document.builder()
                    .content(file.getBytes())
                    .name(file.getOriginalFilename())
                    .size(file.getSize())
                    .mimeType(file.getContentType())
                    .build();
        } catch (IOException ex) {
            log.error(ex.getMessage());
            throw new ApplicationException(ErrorType.DAMAGED_FILE_CONTENT);
        }
        documentRepository.save(document);
        log.info("Saved new document: {}", document.getName());
        return document.getName();
    }

    @Override
    public Document downloadDocument(String documentName) {
        log.info("Downloading document: {}", documentName);
        return documentRepository
                .findByName(documentName)
                .orElseThrow(() -> new ApplicationException(ErrorType.NONEXISTENT_FILE_NAME));

    }

    @Override
    public UUID deleteDocument(String documentName) {
        Document document = documentRepository
                .findByName(documentName)
                .orElseThrow(() -> new ApplicationException(ErrorType.NONEXISTENT_FILE_NAME));
        documentRepository.delete(document);
        log.info("Document {} deleted", documentName);
        return document.getId();
    }
}
