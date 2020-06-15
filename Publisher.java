package project.jdbc;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

/** POJO class for Publisher */
@Builder
@Data
@Accessors(fluent = true)
public class Publisher {
    private String publisherName;
    private String publisherAddress;
    private String publisherPhone;
    private String publisherEmail;
}