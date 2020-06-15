package project.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
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


    /** Private default constructor for singleton pattern */
    private Repository() {
        try {
            connection = DriverManager.getConnection(DB_DRV, DB_USER, DB_PASSWD);
            connection.setSchema(SCHEMA_NAME);
        } catch (SQLException e) {
            System.out.println("Unable to make a connection to the server");
            // TODO: Throw our own exception here
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
    public List<String> getAllWritingGroupNames() throws SQLException {
        List<String> result = new ArrayList<>();
        try (Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT GroupName FROM WritingGroup")) {
            while (rs.next()) {
                result.add(rs.getString("GroupName"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Unable to execute the query 'SELECT GroupName FROM WRITINGGROUP'");
            
        }
        return result;
    }

    public WritingGroup getWritingGroup(String groupName) throws SQLException {
        WritingGroup result = null;
        try (Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(
                "SELECT HeadWriter, YearFormed, Subject FROM WritingGroup WHERE GroupName='" + groupName + "'")) {
            if (rs.next()) {
                result = WritingGroup.builder()
                    .groupName(groupName)
                    .headWriter(rs.getString("HeadWriter"))
                    .subject(rs.getString("Subject"))
                    .yearFormed(rs.getInt("YearFormed"))
                    .build();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Unable to execute the query 'SELECT GroupName FROM WRITINGGROUP'");
            
        }
        return result;
    }

    public List<String> getAllPublisherNames() throws SQLException {
        List<String> result = new ArrayList<>();
        try (Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT PublisherName FROM Publisher")) {
            while (rs.next()) {
                result.add(rs.getString("PublisherName"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Unable to execute the query 'SELECT GroupName FROM WRITINGGROUP'");
            // TODO: Throw query error
        }
        return result;
    }

    public Publisher getPublisher(String publisherName) throws SQLException {
        Publisher result = null;
        try (Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(
            "SELECT PublisherAddress, PublisherPhone, PublisherEmail FROM Publisher "
            + "WHERE PublisherName='" + publisherName + "'")) {
            if (rs.next()) {
                result = Publisher.builder()
                    .publisherName(publisherName)
                    .publisherAddress(rs.getString("PublisherAddress"))
                    .publisherPhone(rs.getString("PublisherPhone"))
                    .publisherEmail(rs.getString("PublisherEmail"))
                    .build();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Unable to execute the query 'SELECT GroupName FROM WRITINGGROUP'");
        }
        return result;
    }

    public List<String> getAllBookTitles() throws SQLException {
        List<String> result = new ArrayList<>();
        try (Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT BookTitle FROM Book")) {
            while (rs.next()) {
                result.add(rs.getString("BookTitle"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Unable to execute the query 'SELECT GroupName FROM WRITINGGROUP'");
            
        }
        return result;
    }

    public Book getBook(String bookTitle, String groupName) throws SQLException {
        Book result = null;
        try (Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(
            "SELECT PublisherName, YearPublished, NumberPages FROM Book " +
            "WHERE GroupName='" + groupName + "' AND BookTitle='" + bookTitle + "'")) {
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
            e.printStackTrace();
            System.out.println("Unable to execute the query 'SELECT GroupName FROM WRITINGGROUP'");
        }
        return result;
    }

    public boolean insertBook(Book book) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            int count = statement.executeUpdate("INSERT INTO Book(GroupName, BookTitle, PublisherName, YearPublished, NumberPages) " +
                "VALUES ('" + book.groupName() + "','" + book.bookTitle() + "','" + 
                book.publisherName() + "'," + book.yearPublished() + "," + book.numberOfPages() + ")");
            return count > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean insertPublisher(Publisher publisher) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            int count = statement.executeUpdate("INSERT INTO Publisher(PublisherName, PublisherAddress, PublisherPhone, PublisherEmail) " +
                "VALUES ('" + publisher.publisherName() + "','" + publisher.publisherAddress() + "','" + 
                publisher.publisherPhone() + "','" + publisher.publisherEmail() + "')");
            return count > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateBookByPublisher(String formerPublisher, String newPublisher) {
        try (Statement statement = connection.createStatement()) {
            int count = statement.executeUpdate(
                "UPDATE Book SET PublisherName='" + newPublisher + "' WHERE PublisherName='" + formerPublisher + "'");
            return count > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean removeBook(String bookTitle, String groupName) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            int count = statement.executeUpdate(
                "DELETE FROM Book where GroupName='" + groupName + "' AND BookTitle='" + bookTitle + "'");
            return count > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}