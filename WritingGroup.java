package project.jdbc;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

/** POJO class for WritingGroup */
@Builder
@Data
@Accessors(fluent = true)
public class WritingGroup {
    private String groupName;
    private String headWriter;
    private String subject;
    private int yearFormed;
}
