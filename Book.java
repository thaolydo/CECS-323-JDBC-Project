package project.jdbc;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

/** POJO class for Book */
@Builder
@Data
@Accessors(fluent = true)
public class Book {
    private String bookTitle;
    private String groupName;
    private String publisherName;
    private int yearPublished;
    private int numberOfPages;
}