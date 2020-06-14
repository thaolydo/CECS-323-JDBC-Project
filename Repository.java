package project.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Repository {
    private static volatile Repository repository;

    private static final String DB_DRV = "jdbc:derby://localhost/Lab Project";
    private static final String DB_USER = "lab_project";
    private static final String DB_PASSWD = "Test1234";

    private Repository() {
    }

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

    public void test() {
        try (Connection connection = DriverManager.getConnection(DB_DRV, DB_USER, DB_PASSWD);
                Statement statement = connection.createStatement()) {
            try (ResultSet resultSet = statement.executeQuery("SELECT * from APP.Books")) {
                // Do stuff with the result set.
                while (resultSet.next()) {
                    System.out.printf("Book(%d, %s)\n", resultSet.getInt(1), resultSet.getString(2));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}