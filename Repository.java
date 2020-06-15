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

public class Repository {
    private static volatile Repository repository;
    
    private Connection connection;
    private static final String DB_DRV = "jdbc:derby://localhost/Lab Project";
    private static final String DB_USER = "lab_project";
    private static final String DB_PASSWD = "Test1234";
    private static final String SCHEMA_NAME = "APP";

    // Prepared statement string
    private static final String GET_WRITING_GROUP_QUERY = "SELECT HeadWriter, YearFormed, Subject FROM WritingGroup WHERE GroupName = ?";
    private static final String GET_PUBLISHER_QUERY = "SELECT PublisherAddress, PublisherPhone, PublisherEmail FROM Publisher WHERE PublisherName = ?";
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

    // TODO: javadoc
    public List<String> getAllWritingGroupNames() {
        List<String> result = new ArrayList<>();
        try (Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT GroupName FROM WritingGroup")) {
            while (rs.next()) {
                result.add(rs.getString("GroupName"));
            }
        } catch (SQLException e) {
            throw new InternalErrorException("Unable to execute the query 'SELECT GroupName FROM WritingGroup'");
        }
        return result;
    }

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

    public List<String> getAllPublisherNames() {
        List<String> result = new ArrayList<>();
        try (Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT PublisherName FROM Publisher")) {
            while (rs.next()) {
                result.add(rs.getString("PublisherName"));
            }
        } catch (SQLException e) {
            throw new InternalErrorException("Unable to execute the query 'SELECT PublisherName FROM Publisher'");
        }
        return result;
    }

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

    public List<String> getAllBookTitles() {
        List<String> result = new ArrayList<>();
        try (Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT BookTitle FROM Book")) {
            while (rs.next()) {
                result.add(rs.getString("BookTitle"));
            }
        } catch (SQLException e) {
            throw new InternalErrorException("Unable to execute the query 'SELECT BookTitle FROM Book'");
        }
        return result;
    }

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

    public boolean insertBook(Book book) {
        try (PreparedStatement statement = connection.prepareStatement(INSERT_BOOK_STATEMENT)) {
            statement.setString(1, book.groupName());
            statement.setString(2, book.bookTitle());
            statement.setString(3, book.publisherName());
            statement.setInt(4, book.yearPublished());
            statement.setInt(5, book.numberOfPages());
            int count = statement.executeUpdate();
            return count > 0;
        } catch (SQLIntegrityConstraintViolationException e) {
            return false;
        } catch (SQLException e) {
            throw new InternalErrorException("Unable to execute the query " + INSERT_BOOK_STATEMENT);
        }
    }

    public boolean insertPublisher(Publisher publisher) {
        try (PreparedStatement statement = connection.prepareStatement(INSERT_PUBLISHER_STATEMENT)) {
            statement.setString(1, publisher.publisherName());
            statement.setString(2, publisher.publisherAddress());
            statement.setString(3, publisher.publisherPhone());
            statement.setString(4, publisher.publisherEmail());
            int count = statement.executeUpdate();
            return count > 0;
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.printf("Failed to insert publisher '%s' because it already exists\n", publisher.publisherName());
            return false;
        } catch (SQLException e) {
            throw new InternalErrorException("Unable to execute the query " + INSERT_PUBLISHER_STATEMENT);
        }
    }

    public boolean updateBookByPublisher(String formerPublisher, String newPublisher) {
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_BOOK_PUBLISHER_STATEMENT)) {
            statement.setString(1, newPublisher);
            statement.setString(2, formerPublisher);
            int count = statement.executeUpdate();
            return count > 0;
        }  catch (SQLException e) {
            throw new InternalErrorException("Unable to execute the query " + UPDATE_BOOK_PUBLISHER_STATEMENT);
        }
    }

    public boolean removeBook(String bookTitle, String groupName) {
        try (PreparedStatement statement = connection.prepareStatement(REMOVE_BOOK_STATEMENT)) {
            statement.setString(1, groupName);
            statement.setString(2, bookTitle);
            int count = statement.executeUpdate();
            return count > 0;
        } catch (SQLIntegrityConstraintViolationException e) {
            return false;
        }  catch (SQLException e) {
            throw new InternalErrorException("Unable to execute the query " + REMOVE_BOOK_STATEMENT);
        }
    }

}