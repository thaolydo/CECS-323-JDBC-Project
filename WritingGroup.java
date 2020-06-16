package project.jdbc;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

/** POJO class for WritingGroup */
@Builder // Builder to build objects
@Data // To String, getters, and setters
@Accessors(fluent = true) // No "get" prefix
public class WritingGroup {
    private String groupName;
    private String headWriter;
    private String subject;
    private int yearFormed;
}
