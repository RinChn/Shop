package marketplace.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@Builder
@AllArgsConstructor
public class DocumentResponse {
    String name;
    Timestamp uploadDate;
    Long size;
    String mimeType;
}
