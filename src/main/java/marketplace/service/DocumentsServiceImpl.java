package marketplace.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marketplace.exception.ApplicationException;
import marketplace.exception.ErrorType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class DocumentsServiceImpl implements DocumentsService {

    @Override
    public List<String> allFilesNames() {
        File[] allFiles = new File("src/main/resources/reports").listFiles();
        List<String> fileNames = new ArrayList<>();
        for (File file : allFiles) {
            fileNames.add(file.getName());
        }
        log.info("Returning all file names: {}", fileNames);
        return fileNames;
    }

    @Override
    public String uploadDocument(MultipartFile multipartFile) {
        Path path = Path.of("src/main/resources/reports",
                Objects.requireNonNull(multipartFile.getOriginalFilename()));
        try {
            multipartFile.transferTo(path);
        } catch (IOException exception) {
            throw new ApplicationException(ErrorType.INVALID_FILE_CONTENT);
        }
        return path.toString();
    }

    @Override
    public MultipartFile downloadDocument(String fileName) {
        File file = new File("src/main/resources/reports",
                fileName);
        FileInputStream input;
        MultipartFile multipartFile;

        if (!file.exists()) {
            throw new ApplicationException(ErrorType.NONEXISTEN_FILE);
        }
        try {
            input = new FileInputStream(file);
        } catch (FileNotFoundException exception) {
            throw new ApplicationException(ErrorType.NONEXISTEN_FILE);
        }
        try {
            multipartFile = new MockMultipartFile(
                    file.getName(),
                    file.getName(),
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                    input
            );
        } catch (IOException exception) {
            throw new ApplicationException(ErrorType.INVALID_FILE_CONTENT);
        }
        return multipartFile;
    }
}
