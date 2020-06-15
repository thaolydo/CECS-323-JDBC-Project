package project.jdbc;

import java.sql.SQLException;
import java.util.Scanner;

public class JDBCRunner {
    private static Scanner scanner = new Scanner(System.in);
    private static Repository repository = Repository.getRepository();
    static final String displayFormat = "%-5s%-15s%-15s%-15s\n"; // from professor's source

    public static void main(String[] args) throws SQLException {
        while (true) {
            int option = displayMenu();
            if (option == 0) {
                break;
            }

            switch (option) {
                case 1:
                    listGroups();
                    break;
                case 2:
                    listGroup();
                    break;
                case 3:
                    listPublishers();
                    break;
                case 4:
                    listPublisher();
                    break;
                case 5:
                    listBookTitles();
                    break;
                case 6:
                    listBookTitle();
                    break;
                case 7:
                    insertBook();
                    break;
                case 8:
                    insertPublisher();
                    break;
                case 9:
                    removeBook();
                    break;
                default:
                    System.out.println("Please choose an option 1-10.");
            }

            clearScreen();
        }

        scanner.close();
        repository.closeConnection();
    }

    private static void listGroups() throws SQLException {
        for (String groupName : repository.getAllWritingGroupNames()) {
            System.out.println("\t" + groupName);
        }
    }

    private static void listGroup() throws SQLException {
        // Get user input
        System.out.print("Enter writting group: ");
        String groupName = scanner.nextLine();

        // Print out result
        System.out.println("\t" + repository.getWritingGroup(groupName));
    }

    private static void listPublishers() throws SQLException {
        for (String publisherName : repository.getAllPublisherNames()) {
            System.out.println("\t" + publisherName);
        }
    }

    private static void listPublisher() throws SQLException {
        // Get user input
        System.out.print("Enter the Publisher: ");
        String publisherName = scanner.nextLine();
        System.out.println("\t" + repository.getPublisher(publisherName));
    }

    private static void listBookTitles() throws SQLException {
        for (String bookTitle : repository.getAllBookTitles()) {
            System.out.println("\t" + bookTitle);
        }
    }

    private static void listBookTitle() throws SQLException {
        // Get user input
        System.out.print("Enter the title of book: ");
        String bookTitle = scanner.nextLine();
        System.out.print("Enter writting group: ");
        String groupName = scanner.nextLine();

        // Print out result
        System.out.println("\t" + repository.getBook(bookTitle, groupName));
    }

    private static void insertBook() throws SQLException {
        // Get user input
        System.out.print("Enter the title of book to insert: ");
        String bookTitle = scanner.nextLine();
        System.out.print("Enter writting group: ");
        String groupName = scanner.nextLine();
        System.out.print("Enter the Publisher: ");
        String publisherName = scanner.nextLine();
        System.out.print("Enter the YearPublished: ");
        int yearPublished = scanner.nextInt();
        System.out.print("Enter the number of pages: ");
        int numberOfPages = scanner.nextInt();
        scanner.nextLine();

        // Build book object from input
        Book book = Book.builder()
            .groupName(groupName)
            .bookTitle(bookTitle)
            .publisherName(publisherName)
            .yearPublished(yearPublished)
            .numberOfPages(numberOfPages)
            .build();

        // Insert to database
        // TODO: make sure writing group and publisher exist before insert
        repository.insertBook(book);
    }

    private static void insertPublisher() throws SQLException {
        // Get user input
        System.out.print("Enter the Publisher name: ");
        String publisherName = scanner.nextLine();
        System.out.print("Enter the address: ");
        String publisherAddress = scanner.nextLine();
        System.out.print("Enter the publisher phone: ");
        String publisherPhone = scanner.nextLine();
        System.out.print("Enter the email : ");
        String publisherEmail = scanner.nextLine();
        System.out.print("Enter the replaced publisher name: ");
        String formerPublisher = scanner.nextLine();

        // Build the publisher object from input
        Publisher publisher = Publisher.builder()
            .publisherName(publisherName)
            .publisherAddress(publisherAddress)
            .publisherPhone(publisherPhone)
            .publisherEmail(publisherEmail)
            .build();

        // Insert and update
        if (repository.insertPublisher(publisher)) {
            System.out.printf("The publisher '%s' has been added successfully\n", publisherName);
        }
        if (repository.updateBookByPublisher(formerPublisher, publisherName)) {
            System.out.printf("The books with publisher '%s' has been replaced with publisher '%s'\n",
                formerPublisher, publisherName);
        }
        // TODO: output if fail any of the two above
    }

    private static void removeBook() throws SQLException {
        System.out.print("Enter title of book to remove: ");
        String bookTitle = scanner.nextLine();
        System.out.print("Enter writting group of book to remove: ");
        String groupName = scanner.nextLine();

        if (repository.removeBook(bookTitle, groupName)) {
            System.out.printf("The book '%s' with writing group '%s' has been removed successfully\n",
                bookTitle, groupName);
        }
        // TODO: handle the exception when book is not removed
    }

    private static int displayMenu() {
        System.out.println("----------------------------------");
        System.out.println("|              Menu              |");
        System.out.println("----------------------------------");
        System.out.println("1. List all writing groups");
        System.out.println("2. List all the data for a group");
        System.out.println("3. List all publishers");
        System.out.println("4. List all the data for a publisher");
        System.out.println("5. List all book titles");
        System.out.println("6. List all the data for a book");
        System.out.println("7. Insert a new book");
        System.out.println("8. Insert a new publisher and update all books published by one publisher to be");
        System.out.println("9. Remove a book");
        System.out.println("0. Quit");
        System.out.println();
        System.out.print("Choose option: ");

        int option = scanner.nextInt();
        scanner.nextLine();
        return option;
    }

    private static void clearScreen() {
        System.out.println();
        System.out.print("Press 'ENTER' to continue");
        scanner.nextLine();
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
