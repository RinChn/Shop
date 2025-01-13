package marketplace.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "document")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    UUID id;
    @Column(name = "name", nullable = false, length = 255)
    String name;
    @Column(name = "content", columnDefinition = "bytea")
    byte[] content;
    @Column(name = "upload_date", columnDefinition = "timestamp default CURRENT_TIMESTAMP")
    @Builder.Default
    Timestamp uploadDate = new Timestamp(System.currentTimeMillis());
    @Column(name = "size")
    Long size;
    @Column(name = "mime_type")
    String mimeType;
}
