package project.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is to communicate with Database.
 */
public class Repository {
    private static volatile Repository repository;
    
    private Connection connection;
    private static final String DB_DRV = "jdbc:derby://localhost/Lab Project";
    private static final String DB_USER = "lab_project";
    private static final String DB_PASSWD = "Test1234";
    private static final String SCHEMA_NAME = "APP";

    // Prepared statement string
    private static final String GET_WRITING_GROUPS_QUERY = "SELECT GroupName, HeadWriter, YearFormed, Subject FROM WritingGroup ORDER BY GroupName";
    private static final String GET_WRITING_GROUP_QUERY = "SELECT HeadWriter, YearFormed, Subject FROM WritingGroup WHERE GroupName = ?";
    private static final String GET_PUBLISHERS_QUERY = "SELECT PublisherName, PublisherAddress, PublisherPhone, PublisherEmail FROM Publisher ORDER BY PublisherName";
    private static final String GET_PUBLISHER_QUERY = "SELECT PublisherAddress, PublisherPhone, PublisherEmail FROM Publisher WHERE PublisherName = ?";
    private static final String GET_BOOKS_QUERY = "SELECT BookTitle, GroupName, PublisherName, YearPublished, NumberPages FROM Book ORDER BY BookTitle";
    private static final String GET_BOOK_QUERY = "SELECT PublisherName, YearPublished, NumberPages FROM Book " +
        "WHERE GroupName = ? AND BookTitle = ?";
    private static final String INSERT_BOOK_STATEMENT = "INSERT INTO Book(GroupName, BookTitle, PublisherName, YearPublished, NumberPages) VALUES (?, ?, ?, ?, ?)";
    private static final String INSERT_PUBLISHER_STATEMENT = "INSERT INTO Publisher(PublisherName, PublisherAddress, PublisherPhone, PublisherEmail) VALUES(?, ?, ?, ?)";
    private static final String UPDATE_BOOK_PUBLISHER_STATEMENT = "UPDATE Book SET PublisherName = ? WHERE PublisherName = ?";
    private static final String REMOVE_BOOK_STATEMENT = "DELETE FROM Book where GroupName = ? AND BookTitle = ?";


    /** Private default constructor for singleton pattern */
    private Repository() {
        try {
            connection = DriverManager.getConnection(DB_DRV, DB_USER, DB_PASSWD);
            connection.setSchema(SCHEMA_NAME);
        } catch (SQLException e) {
            throw new InternalErrorException("Unable to make a connection to the server");
        }
    }

    /** Close connection */
    public void closeConnection() {
        try {
            this.connection.close();
        } catch (SQLException e) {
            throw new InternalErrorException("Unable to close connection");
        }
    }

    /**
     * This method is to get the singleton instance for this class.
     * 
     * @return singleton instance for Repository
     */
    public static Repository getRepository() {
        if (repository == null) {
            synchronized (Repository.class) {
                if (repository == null) {
                    repository = new Repository();
                }
            }
        }
        return repository;
    }

    /**
     * This method is to retrieve all records in the WritingGroup table.
     * 
     * @return all records in the WritingGroup table.
     */
    public List<WritingGroup> getAllWritingGroupNames() {
        List<WritingGroup> result = new ArrayList<>();
        try (Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(GET_WRITING_GROUPS_QUERY)) {
            while (rs.next()) {
                WritingGroup group = WritingGroup.builder()
                    .groupName(rs.getString("GroupName"))
                    .headWriter(rs.getString("HeadWriter"))
                    .yearFormed(rs.getInt("YearFormed"))
                    .subject(rs.getString("Subject"))
                    .build();

                result.add(group);
            }
        } catch (SQLException e) {
            throw new InternalErrorException("Unable to execute the query " + GET_WRITING_GROUPS_QUERY);
        }
        return result;
    }

    /**
     * This method is to retrieve a writing group specified by the given group name.
     * 
     * @param groupName the primary key of the WritingGroup table
     * @return the group specified by group name
     */
    public WritingGroup getWritingGroup(String groupName) {
        WritingGroup result = null;
        try (PreparedStatement statement = connection.prepareStatement(GET_WRITING_GROUP_QUERY)) {
            statement.setString(1, groupName);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    result = WritingGroup.builder()
                        .groupName(groupName)
                        .headWriter(rs.getString("HeadWriter"))
                        .subject(rs.getString("Subject"))
                        .yearFormed(rs.getInt("YearFormed"))
                        .build();
                }
            } catch (SQLException e) {
                throw new InternalErrorException("Unable to execute the query " + GET_WRITING_GROUP_QUERY);
            }
        } catch (SQLException e) {
            throw new InternalErrorException("Unable to create prepared statement " + GET_WRITING_GROUP_QUERY);
        }
        return result;
    }

    /**
     * This method is to retrieve all records in the Publisher table.
     * 
     * @return all records in the Publisher table.
     */
    public List<Publisher> getAllPublisherNames() {
        List<Publisher> result = new ArrayList<>();
        try (Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(GET_PUBLISHERS_QUERY)) {
            while (rs.next()) {
                Publisher publisher = Publisher.builder()
                    .publisherName(rs.getString("PublisherName"))
                    .publisherEmail(rs.getString("PublisherEmail"))
                    .publisherPhone(rs.getString("PublisherPhone"))
                    .publisherAddress(rs.getString("PublisherAddress"))
                    .build();
                result.add(publisher);
            }
        } catch (SQLException e) {
            throw new InternalErrorException("Unable to execute the query " + GET_PUBLISHERS_QUERY);
        }
        return result;
    }

    /**
     * This method is to retrieve a publisher specified by the given publisher name.
     * 
     * @param publisher the primary key of the publisher table
     * @return the pulisher specified by publisher name
     */
    public Publisher getPublisher(String publisherName) {
        Publisher result = null;
        try (PreparedStatement statement = connection.prepareStatement(GET_PUBLISHER_QUERY)) {
            statement.setString(1, publisherName);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    result = Publisher.builder()
                        .publisherName(publisherName)
                        .publisherAddress(rs.getString("PublisherAddress"))
                        .publisherPhone(rs.getString("PublisherPhone"))
                        .publisherEmail(rs.getString("PublisherEmail"))
                        .build();
                }
            } catch (SQLException e) {
                throw new InternalErrorException("Unable to execute the query " + GET_PUBLISHER_QUERY);
            }
        } catch (SQLException e) {
            throw new InternalErrorException("Unable to create the prepared statement " + GET_PUBLISHER_QUERY);
        }
        return result;
    }

    /**
     * This method is to retrieve all records in the Book table.
     * 
     * @return all records in the Book table.
     */
    public List<Book> getAllBookTitles() {
        List<Book> result = new ArrayList<>();
        try (Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(GET_BOOKS_QUERY)) {
            while (rs.next()) {
                Book book = Book.builder()
                    .bookTitle(rs.getString("BookTitle"))
                    .groupName(rs.getString("GroupName"))
                    .publisherName(rs.getString("PublisherName"))
                    .numberOfPages(rs.getInt("NumberPages"))
                    .yearPublished(rs.getInt("YearPublished"))
                    .build();
                result.add(book);
            }
        } catch (SQLException e) {
            throw new InternalErrorException("Unable to execute the query " + GET_BOOKS_QUERY, e);
        }
        return result;
    }

    /**
     * This method is to retrieve the book specified by the given group name and book title.
     * 
     * @param bookTitle title of the book
     * @param groupName the writing group of the book
     * @return the book specified by the group name and title
     */
    public Book getBook(String bookTitle, String groupName) {
        Book result = null;
        try (PreparedStatement statement = connection.prepareStatement(GET_BOOK_QUERY)) {
            statement.setString(1, groupName);
            statement.setString(2, bookTitle);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    result = Book.builder()
                        .bookTitle(bookTitle)
                        .groupName(groupName)
                        .publisherName(rs.getString("PublisherName"))
                        .yearPublished(rs.getInt("YearPublished"))
                        .numberOfPages(rs.getInt("NumberPages"))
                        .build();
                }
            } catch (SQLException e) {
                throw new InternalErrorException("Unable to execute the query " + GET_BOOK_QUERY);
            }
        } catch (SQLException e) {
            throw new InternalErrorException("Unable to create prepared statement for " + GET_BOOK_QUERY);
        }
        return result;
    }

    /**
     * This method is to insert the given book to the Book table.
     * 
     * @param book the book POJO
     * @return true if success, false otherwise
     */
    public boolean insertBook(Book book) {
        try (PreparedStatement statement = connection.prepareStatement(INSERT_BOOK_STATEMENT)) {
            statement.setString(1, book.groupName());
            statement.setString(2, book.bookTitle());
            statement.setString(3, book.publisherName());
            statement.setInt(4, book.yearPublished());
            statement.setInt(5, book.numberOfPages());
            statement.executeUpdate();
            return true;
        } catch (SQLIntegrityConstraintViolationException e) {
            return false;
        } catch (SQLException e) {
            throw new InternalErrorException("Unable to execute the query " + INSERT_BOOK_STATEMENT);
        }
    }

    /**
     * This method is to insert the given publisher to the Publisher table.
     * 
     * @param publisher the publisher POJO
     * @return true if success, false otherwise
     */
    public boolean insertPublisher(Publisher publisher) {
        try (PreparedStatement statement = connection.prepareStatement(INSERT_PUBLISHER_STATEMENT)) {
            statement.setString(1, publisher.publisherName());
            statement.setString(2, publisher.publisherAddress());
            statement.setString(3, publisher.publisherPhone());
            statement.setString(4, publisher.publisherEmail());
            statement.executeUpdate();
            return true;
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.printf("Failed to insert publisher '%s' because it already exists\n", publisher.publisherName());
            return false;
        } catch (SQLException e) {
            throw new InternalErrorException("Unable to execute the query " + INSERT_PUBLISHER_STATEMENT);
        }
    }

    /**
     * This method is to update all book by former publisher with the new publisher
     * @param formerPublisher the former publisher name
     * @param newPublisher the new publisher name
     * @return true if success, false otherwise
     */
    public boolean updateBookByPublisher(String formerPublisher, String newPublisher) {
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_BOOK_PUBLISHER_STATEMENT)) {
            statement.setString(1, newPublisher);
            statement.setString(2, formerPublisher);
            statement.executeUpdate();
            return true;
        }  catch (SQLException e) {
            throw new InternalErrorException("Unable to execute the query " + UPDATE_BOOK_PUBLISHER_STATEMENT);
        }
    }

    /**
     * This method is to remove the book from the table
     * @param bookTitle the book title
     * @param groupName the group name
     * @return true if success, false otherwise
     */
    public boolean removeBook(String bookTitle, String groupName) {
        try (PreparedStatement statement = connection.prepareStatement(REMOVE_BOOK_STATEMENT)) {
            statement.setString(1, groupName);
            statement.setString(2, bookTitle);
            statement.executeUpdate();
            return true;
        } catch (SQLIntegrityConstraintViolationException e) {
            return false;
        }  catch (SQLException e) {
            throw new InternalErrorException("Unable to execute the query " + REMOVE_BOOK_STATEMENT);
        }
    }

}