package marketplace.converter;

import marketplace.dto.DocumentResponse;
import marketplace.entity.Document;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class DocumentToDtoConverter implements Converter<Document, DocumentResponse> {

    @Override
    public DocumentResponse convert(Document source) {
        return DocumentResponse.builder()
                .name(source.getName())
                .size(source.getSize())
                .mimeType(source.getMimeType())
                .uploadDate(source.getUploadDate())
                .build();
    }
}
