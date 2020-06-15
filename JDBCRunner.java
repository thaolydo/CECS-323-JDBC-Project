package project.jdbc;

import java.util.Scanner;

public class JDBCRunner {
    private static Scanner scanner = new Scanner(System.in);
    private static Repository repository = Repository.getRepository();
    static final String displayFormat = "%-5s%-15s%-15s%-15s\n"; // from professor's source

    public static void main(String[] args) throws InternalErrorException {
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

    private static void listGroups() {
        for (String groupName : repository.getAllWritingGroupNames()) {
            System.out.println("\t" + groupName);
        }
    }

    private static void listGroup() {
        // Get user input
        System.out.print("Enter writting group: ");
        String groupName = scanner.nextLine();

        // Print out result
        System.out.println("\t" + repository.getWritingGroup(groupName));
    }

    private static void listPublishers() {
        for (String publisherName : repository.getAllPublisherNames()) {
            System.out.println("\t" + publisherName);
        }
    }

    private static void listPublisher() {
        // Get user input
        System.out.print("Enter the Publisher: ");
        String publisherName = scanner.nextLine();
        System.out.println("\t" + repository.getPublisher(publisherName));
    }

    private static void listBookTitles() {
        for (String bookTitle : repository.getAllBookTitles()) {
            System.out.println("\t" + bookTitle);
        }
    }

    private static void listBookTitle() {
        // Get user input
        System.out.print("Enter the title of book: ");
        String bookTitle = scanner.nextLine();
        System.out.print("Enter writting group: ");
        String groupName = scanner.nextLine();

        // Print out result
        System.out.println("\t" + repository.getBook(bookTitle, groupName));
    }

    private static void insertBook() {
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

        // Check if publisher exist
        if (repository.getPublisher(publisherName) == null) {
            System.out.printf("Publisher '%s' does not exist\n", publisherName);
            return;
        }

        // Check if writing group exist
        if (repository.getWritingGroup(groupName) == null) {
            System.out.printf("Writing group '%s' does not exist\n", groupName);
            return;
        }
        
        // Check if book already exist
        if (repository.getBook(bookTitle, groupName) != null) {
            System.out.printf("The book with title '%s' and writing group '%s' alread exists\n", bookTitle, groupName);
            return;
        }
        
        // Insert to database
        if (repository.insertBook(book)) {
            System.out.printf("The book '%s' has been added successfully\n", bookTitle);
        } else {
            System.out.printf("Failed to add the book '%s' with publisher '%s' because book title and publisher must be unique\n",
                bookTitle, publisherName);
        }
    }

    private static void insertPublisher() {
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

        // Insert publisher
        if (!repository.insertPublisher(publisher)) {
            System.out.printf("Failed to add. The publisher '%s' already exist", publisherName);
            return;
        }
        System.out.printf("The publisher '%s' has been added successfully", publisherName);
        
        // Update books with new publisher
        if (!repository.updateBookByPublisher(formerPublisher, publisherName)) {
            System.out.printf("Failed to update books with former publisher '%s' and new publisher '%s'.", publisherName);
            return;
        }
        System.out.printf("The books with publisher '%s' has been replaced with publisher '%s' successfully\n",
            formerPublisher, publisherName);
    }

    private static void removeBook() {
        System.out.print("Enter title of book to remove: ");
        String bookTitle = scanner.nextLine();
        System.out.print("Enter writting group of book to remove: ");
        String groupName = scanner.nextLine();

        // Check if writing group exist
        if (repository.getWritingGroup(groupName) == null) {
            System.out.printf("Writing group '%s' does not exist\n", groupName);
            return;
        }

        // Check if the book exist
        if (repository.getBook(bookTitle, groupName) == null) {
            System.out.printf("The book with title '%s' and writing group '%s' does not exist\n", bookTitle, groupName);
            return;
        }

        if (repository.removeBook(bookTitle, groupName)) {
            System.out.printf("The book '%s' with writing group '%s' has been removed successfully\n",
                bookTitle, groupName);
        } else {
            System.out.printf("Failed to remove the book '%s' with wring group '%s'\n", bookTitle, groupName);
        }
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
