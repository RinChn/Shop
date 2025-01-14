package marketplace.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
}
