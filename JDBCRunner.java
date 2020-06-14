package project.jdbc;

import java.util.Scanner;

public class JDBCRunner {
    private static Scanner scanner = new Scanner(System.in);
    private static Repository repository = Repository.getRepository();

    public static void main(String[] args) {
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
    }

    private static void listGroups() {
        repository.test();
    }

    private static void listGroup() {
    }

    private static void listPublishers() {
    }

    private static void listPublisher() {
    }

    private static void listBookTitles() {
    }

    private static void listBookTitle() {
    }

    private static void insertBook() {
    }

    private static void insertPublisher() {
    }

    private static void removeBook() {
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
        return scanner.nextInt();
    }

    private static void clearScreen() {
        System.out.println();
        System.out.print("Press 'ENTER' to continue");
        scanner.nextLine();
        scanner.nextLine();
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}