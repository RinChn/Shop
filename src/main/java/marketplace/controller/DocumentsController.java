package marketplace.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marketplace.service.DocumentsServiceImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
}
