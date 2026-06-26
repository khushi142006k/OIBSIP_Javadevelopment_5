package com.library;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles all menu displays, navigation, and user-facing interactions.
 */
public class Menu {
    private Library library;
    private Authentication auth;

    public Menu(Library library, Authentication auth) {
        this.library = library;
        this.auth = auth;
    }

    /**
     * Entry point to run the command line interface.
     */
    public void start() {
        boolean running = true;
        while (running) {
            Utility.clearScreen();
            Utility.printBanner();
            System.out.println("1. Admin Login");
            System.out.println("2. User Login");
            System.out.println("3. Register New User");
            System.out.println("4. Exit Program");
            Utility.printDivider();

            int choice = Utility.readInt("Enter choice (1-4): ", 1, 4);
            switch (choice) {
                case 1:
                    handleAdminLogin();
                    break;
                case 2:
                    handleUserLogin();
                    break;
                case 3:
                    handleUserRegistration(true); // From main menu
                    break;
                case 4:
                    running = false;
                    Utility.printlnColored("\nThank you for using Digital Library Management System. Goodbye!", Utility.ANSI_GREEN);
                    break;
            }
        }
    }

    // --- MAIN MENU HANDLERS ---

    private void handleAdminLogin() {
        Utility.clearScreen();
        Utility.printBanner();
        Utility.printlnColored("=== Administrator Login ===", Utility.ANSI_BOLD + Utility.ANSI_CYAN);
        String username = Utility.readString("Enter Username: ");
        String password = Utility.readString("Enter Password: ");

        if (auth.loginAdmin(username, password, library.getAdmins())) {
            Utility.printlnColored("\nLogin Successful! Welcome, " + auth.getCurrentAdmin().getName() + ".", Utility.ANSI_GREEN);
            Utility.pressEnterToContinue();
            showAdminMenu();
        } else {
            Utility.printlnColored("\nInvalid username or password.", Utility.ANSI_RED);
            Utility.pressEnterToContinue();
        }
    }

    private void handleUserLogin() {
        Utility.clearScreen();
        Utility.printBanner();
        Utility.printlnColored("=== User Login ===", Utility.ANSI_BOLD + Utility.ANSI_CYAN);
        String username = Utility.readString("Enter Username: ");
        String password = Utility.readString("Enter Password: ");

        if (auth.loginUser(username, password, library.getUsers())) {
            Utility.printlnColored("\nLogin Successful! Welcome, " + auth.getCurrentUser().getName() + ".", Utility.ANSI_GREEN);
            Utility.pressEnterToContinue();
            showUserMenu();
        } else {
            Utility.printlnColored("\nInvalid username or password.", Utility.ANSI_RED);
            Utility.pressEnterToContinue();
        }
    }

    private void handleUserRegistration(boolean fromMainMenu) {
        Utility.clearScreen();
        Utility.printBanner();
        Utility.printlnColored("=== Register New User ===", Utility.ANSI_BOLD + Utility.ANSI_CYAN);
        String name = Utility.readString("Enter Full Name: ");
        String email = Utility.readEmail("Enter Email Address: ");

        String username;
        while (true) {
            username = Utility.readString("Choose Username: ");
            if (library.findUserByUsername(username) == null && library.findAdminByUsername(username) == null) {
                break;
            }
            Utility.printlnColored("Username already taken. Please choose another.", Utility.ANSI_RED);
        }
        String password = Utility.readString("Choose Password: ");

        User newUser = new User(username, password, name, email);
        if (library.registerUser(newUser)) {
            Utility.printlnColored("\nUser registration successful!", Utility.ANSI_GREEN);
        } else {
            Utility.printlnColored("\nRegistration failed.", Utility.ANSI_RED);
        }
        Utility.pressEnterToContinue();
    }

    // --- ADMIN SUBMENU ---

    private void showAdminMenu() {
        boolean inAdminSession = true;
        while (inAdminSession && auth.isAdminLoggedIn()) {
            Utility.clearScreen();
            Utility.printBanner();
            Utility.printlnColored("Admin Session: " + auth.getCurrentAdmin().getName(), Utility.ANSI_BOLD + Utility.ANSI_BLUE);
            Utility.printDivider();
            System.out.println("1. Add New Book");
            System.out.println("2. Update Book Details");
            System.out.println("3. Delete Book");
            System.out.println("4. View All Books");
            System.out.println("5. Search Book");
            System.out.println("6. Register New User");
            System.out.println("7. View All Users");
            System.out.println("8. Issue Book");
            System.out.println("9. Return Book");
            System.out.println("10. Display All Issued Books");
            System.out.println("11. Book Availability Status");
            System.out.println("12. Generate Reports");
            System.out.println("13. Register New Admin");
            System.out.println("14. Logout");
            Utility.printDivider();

            int choice = Utility.readInt("Enter choice (1-14): ", 1, 14);
            switch (choice) {
                case 1:
                    adminAddBook();
                    break;
                case 2:
                    adminUpdateBook();
                    break;
                case 3:
                    adminDeleteBook();
                    break;
                case 4:
                    viewAllBooks();
                    break;
                case 5:
                    searchBooks();
                    break;
                case 6:
                    handleUserRegistration(false);
                    break;
                case 7:
                    adminViewAllUsers();
                    break;
                case 8:
                    adminIssueBook();
                    break;
                case 9:
                    adminReturnBook();
                    break;
                case 10:
                    adminDisplayIssuedBooks();
                    break;
                case 11:
                    checkBookAvailability();
                    break;
                case 12:
                    adminGenerateReports();
                    break;
                case 13:
                    adminRegisterNewAdmin();
                    break;
                case 14:
                    auth.logout();
                    inAdminSession = false;
                    Utility.printlnColored("\nLogged out successfully.", Utility.ANSI_GREEN);
                    Utility.pressEnterToContinue();
                    break;
            }
        }
    }

    private void adminAddBook() {
        Utility.clearScreen();
        Utility.printBanner();
        Utility.printlnColored("=== Add New Book ===", Utility.ANSI_BOLD + Utility.ANSI_CYAN);
        String id = Utility.readString("Enter Book ID: ");
        String title = Utility.readString("Enter Book Title: ");
        String author = Utility.readString("Enter Book Author: ");

        Book book = new Book(id, title, author);
        if (library.addBook(book)) {
            Utility.printlnColored("\nBook added successfully!", Utility.ANSI_GREEN);
        } else {
            Utility.printlnColored("\nError: A book with this ID already exists.", Utility.ANSI_RED);
        }
        Utility.pressEnterToContinue();
    }

    private void adminUpdateBook() {
        Utility.clearScreen();
        Utility.printBanner();
        Utility.printlnColored("=== Update Book Details ===", Utility.ANSI_BOLD + Utility.ANSI_CYAN);
        String id = Utility.readString("Enter Book ID to Update: ");
        Book book = library.findBookById(id);

        if (book == null) {
            Utility.printlnColored("\nBook not found.", Utility.ANSI_RED);
            Utility.pressEnterToContinue();
            return;
        }

        System.out.println("\nCurrent Details: " + book);
        String title = Utility.readOptionalString("Enter New Title (leave blank to keep current): ");
        String author = Utility.readOptionalString("Enter New Author (leave blank to keep current): ");

        if (library.updateBook(id, title, author)) {
            Utility.printlnColored("\nBook updated successfully!", Utility.ANSI_GREEN);
        } else {
            Utility.printlnColored("\nFailed to update book.", Utility.ANSI_RED);
        }
        Utility.pressEnterToContinue();
    }

    private void adminDeleteBook() {
        Utility.clearScreen();
        Utility.printBanner();
        Utility.printlnColored("=== Delete Book ===", Utility.ANSI_BOLD + Utility.ANSI_CYAN);
        String id = Utility.readString("Enter Book ID to Delete: ");

        Book book = library.findBookById(id);
        if (book == null) {
            Utility.printlnColored("\nBook not found.", Utility.ANSI_RED);
            Utility.pressEnterToContinue();
            return;
        }

        System.out.println("Book to Delete: " + book);
        String confirm = Utility.readString("Are you sure you want to delete this book? (yes/no): ");
        if (confirm.equalsIgnoreCase("yes") || confirm.equalsIgnoreCase("y")) {
            if (library.deleteBook(id)) {
                Utility.printlnColored("\nBook deleted successfully!", Utility.ANSI_GREEN);
            } else {
                Utility.printlnColored("\nFailed to delete. The book might be currently issued.", Utility.ANSI_RED);
            }
        } else {
            Utility.printlnColored("\nDeletion cancelled.", Utility.ANSI_YELLOW);
        }
        Utility.pressEnterToContinue();
    }

    private void adminViewAllUsers() {
        Utility.clearScreen();
        Utility.printBanner();
        Utility.printlnColored("=== Registered Users ===", Utility.ANSI_BOLD + Utility.ANSI_CYAN);

        List<User> users = library.getUsers();
        if (users.isEmpty()) {
            Utility.printlnColored("No registered users found.", Utility.ANSI_YELLOW);
        } else {
            List<String[]> rows = new ArrayList<>();
            for (User u : users) {
                rows.add(new String[]{u.getUsername(), u.getName(), u.getEmail()});
            }
            Utility.printTable(new String[]{"Username", "Full Name", "Email"}, rows);
        }
        Utility.pressEnterToContinue();
    }

    private void adminIssueBook() {
        Utility.clearScreen();
        Utility.printBanner();
        Utility.printlnColored("=== Issue Book ===", Utility.ANSI_BOLD + Utility.ANSI_CYAN);
        String bookId = Utility.readString("Enter Book ID: ");
        String username = Utility.readString("Enter User Username: ");

        String[] errorMsg = new String[1];
        if (library.issueBook(bookId, username, errorMsg)) {
            Utility.printlnColored("\nBook issued successfully!", Utility.ANSI_GREEN);
        } else {
            Utility.printlnColored("\nError: " + errorMsg[0], Utility.ANSI_RED);
        }
        Utility.pressEnterToContinue();
    }

    private void adminReturnBook() {
        Utility.clearScreen();
        Utility.printBanner();
        Utility.printlnColored("=== Return Book ===", Utility.ANSI_BOLD + Utility.ANSI_CYAN);
        String bookId = Utility.readString("Enter Book ID: ");

        String[] errorMsg = new String[1];
        double fine = library.returnBook(bookId, errorMsg);

        if (fine >= 0) {
            Utility.printlnColored("\nBook returned successfully!", Utility.ANSI_GREEN);
            if (fine > 0) {
                Utility.printlnColored("Late Return Fine Calculated: $" + String.format("%.2f", fine), Utility.ANSI_RED);
                Utility.printlnColored("Fine marked as PAID.", Utility.ANSI_GREEN);
            } else {
                Utility.printlnColored("Returned on time. No fine calculated.", Utility.ANSI_GREEN);
            }
        } else {
            Utility.printlnColored("\nError: " + errorMsg[0], Utility.ANSI_RED);
        }
        Utility.pressEnterToContinue();
    }

    private void adminDisplayIssuedBooks() {
        Utility.clearScreen();
        Utility.printBanner();
        Utility.printlnColored("=== All Issued Books (Active/Returned) ===", Utility.ANSI_BOLD + Utility.ANSI_CYAN);

        List<IssueRecord> issues = library.getIssues();
        if (issues.isEmpty()) {
            Utility.printlnColored("No lending history found.", Utility.ANSI_YELLOW);
        } else {
            displayIssuesTable(issues);
        }
        Utility.pressEnterToContinue();
    }

    private void adminGenerateReports() {
        Utility.clearScreen();
        Utility.printBanner();
        Utility.printlnColored("=== Console Report ===", Utility.ANSI_BOLD + Utility.ANSI_CYAN);

        int totalBooks = library.getBooks().size();
        int activeIssuesCount = library.getActiveIssues().size();
        int availableBooks = totalBooks - activeIssuesCount;
        int totalUsers = library.getUsers().size();

        double totalFinesCollected = 0.0;
        double outstandingFines = 0.0;
        LocalDate today = LocalDate.now();

        List<IssueRecord> overdueRecords = new ArrayList<>();

        for (IssueRecord r : library.getIssues()) {
            if (r.isReturned()) {
                totalFinesCollected += r.getFinePaid();
            } else {
                double currentFine = r.calculateFine(today);
                if (currentFine > 0) {
                    outstandingFines += currentFine;
                    overdueRecords.add(r);
                }
            }
        }

        System.out.println("\n--- Summary Metrics ---");
        System.out.printf("Total Books registered:  %d\n", totalBooks);
        System.out.printf("Available Books:          %d\n", availableBooks);
        System.out.printf("Active Issued Books:      %d (%.1f%% of library)\n",
                activeIssuesCount, totalBooks > 0 ? (activeIssuesCount * 100.0 / totalBooks) : 0);
        System.out.printf("Total Registered Users:  %d\n", totalUsers);
        System.out.println("------------------------");
        System.out.printf("Total Fines Collected:   $%.2f\n", totalFinesCollected);
        System.out.printf("Total Outstanding Fines: $%.2f\n", outstandingFines);
        System.out.println("------------------------\n");

        if (!overdueRecords.isEmpty()) {
            Utility.printlnColored("⚠️  Overdue Books List:", Utility.ANSI_BOLD + Utility.ANSI_RED);
            displayIssuesTable(overdueRecords);
        } else {
            Utility.printlnColored("✓ No overdue books currently.", Utility.ANSI_GREEN);
        }

        Utility.pressEnterToContinue();
    }

    private void adminRegisterNewAdmin() {
        Utility.clearScreen();
        Utility.printBanner();
        Utility.printlnColored("=== Register New Admin ===", Utility.ANSI_BOLD + Utility.ANSI_CYAN);
        String name = Utility.readString("Enter Full Name: ");
        String username;
        while (true) {
            username = Utility.readString("Choose Admin Username: ");
            if (library.findAdminByUsername(username) == null && library.findUserByUsername(username) == null) {
                break;
            }
            Utility.printlnColored("Username already taken. Please choose another.", Utility.ANSI_RED);
        }
        String password = Utility.readString("Choose Admin Password: ");

        Admin newAdmin = new Admin(username, password, name);
        if (library.registerAdmin(newAdmin)) {
            Utility.printlnColored("\nAdministrator registered successfully!", Utility.ANSI_GREEN);
        } else {
            Utility.printlnColored("\nRegistration failed.", Utility.ANSI_RED);
        }
        Utility.pressEnterToContinue();
    }

    // --- USER SUBMENU ---

    private void showUserMenu() {
        boolean inUserSession = true;
        while (inUserSession && auth.isUserLoggedIn()) {
            Utility.clearScreen();
            Utility.printBanner();
            Utility.printlnColored("User Session: " + auth.getCurrentUser().getName(), Utility.ANSI_BOLD + Utility.ANSI_BLUE);
            Utility.printDivider();
            System.out.println("1. View All Books");
            System.out.println("2. Search Book");
            System.out.println("3. Check Book Availability");
            System.out.println("4. View My Issued Books");
            System.out.println("5. View My Fine Summary");
            System.out.println("6. Logout");
            Utility.printDivider();

            int choice = Utility.readInt("Enter choice (1-6): ", 1, 6);
            switch (choice) {
                case 1:
                    viewAllBooks();
                    break;
                case 2:
                    searchBooks();
                    break;
                case 3:
                    checkBookAvailability();
                    break;
                case 4:
                    userViewIssuedBooks();
                    break;
                case 5:
                    userViewFines();
                    break;
                case 6:
                    auth.logout();
                    inUserSession = false;
                    Utility.printlnColored("\nLogged out successfully.", Utility.ANSI_GREEN);
                    Utility.pressEnterToContinue();
                    break;
            }
        }
    }

    private void userViewIssuedBooks() {
        Utility.clearScreen();
        Utility.printBanner();
        String username = auth.getCurrentUser().getUsername();
        Utility.printlnColored("=== My Borrowed Books ===", Utility.ANSI_BOLD + Utility.ANSI_CYAN);

        List<IssueRecord> userIssues = library.getAllIssuesByUser(username);
        if (userIssues.isEmpty()) {
            Utility.printlnColored("You have no book checkout history.", Utility.ANSI_YELLOW);
        } else {
            displayIssuesTable(userIssues);
        }
        Utility.pressEnterToContinue();
    }

    private void userViewFines() {
        Utility.clearScreen();
        Utility.printBanner();
        String username = auth.getCurrentUser().getUsername();
        Utility.printlnColored("=== Fine Summary ===", Utility.ANSI_BOLD + Utility.ANSI_CYAN);

        double totalFine = library.getTotalFinesByUser(username);
        System.out.printf("\nYour total paid & outstanding overdue fines: $%.2f\n", totalFine);

        // Break down
        List<IssueRecord> history = library.getAllIssuesByUser(username);
        List<IssueRecord> activeLate = new ArrayList<>();
        double activeLateTotal = 0.0;
        LocalDate today = LocalDate.now();

        for (IssueRecord r : history) {
            if (!r.isReturned() && r.calculateFine(today) > 0) {
                activeLate.add(r);
                activeLateTotal += r.calculateFine(today);
            }
        }

        if (activeLateTotal > 0) {
            Utility.printlnColored("\nOutstanding Fines breakdown (Current Borrowed Books late):", Utility.ANSI_RED);
            displayIssuesTable(activeLate);
            System.out.printf("Total Outstanding: $%.2f\n", activeLateTotal);
        } else {
            Utility.printlnColored("\n✓ No outstanding fines.", Utility.ANSI_GREEN);
        }
        Utility.pressEnterToContinue();
    }

    // --- SHARED ACTIONS ---

    private void viewAllBooks() {
        Utility.clearScreen();
        Utility.printBanner();
        Utility.printlnColored("=== Library Books Catalog ===", Utility.ANSI_BOLD + Utility.ANSI_CYAN);

        List<Book> books = library.getBooks();
        if (books.isEmpty()) {
            Utility.printlnColored("No books found in the library catalog.", Utility.ANSI_YELLOW);
        } else {
            displayBooksTable(books);
        }
        Utility.pressEnterToContinue();
    }

    private void searchBooks() {
        Utility.clearScreen();
        Utility.printBanner();
        Utility.printlnColored("=== Search Catalog ===", Utility.ANSI_BOLD + Utility.ANSI_CYAN);
        String query = Utility.readString("Enter search keyword (ID, Title, or Author): ");

        List<Book> results = library.searchBooks(query);
        if (results.isEmpty()) {
            Utility.printlnColored("\nNo matching books found.", Utility.ANSI_YELLOW);
        } else {
            Utility.printlnColored("\nSearch Results (" + results.size() + " books found):", Utility.ANSI_GREEN);
            displayBooksTable(results);
        }
        Utility.pressEnterToContinue();
    }

    private void checkBookAvailability() {
        Utility.clearScreen();
        Utility.printBanner();
        Utility.printlnColored("=== Check Book Availability ===", Utility.ANSI_BOLD + Utility.ANSI_CYAN);
        String bookId = Utility.readString("Enter Book ID: ");

        Book book = library.findBookById(bookId);
        if (book == null) {
            Utility.printlnColored("\nBook not found in library catalog.", Utility.ANSI_RED);
        } else {
            System.out.println("\nBook Details:");
            System.out.println("Title:  " + book.getTitle());
            System.out.println("Author: " + book.getAuthor());
            if (book.isAvailable()) {
                Utility.printlnColored("Status: AVAILABLE ✅", Utility.ANSI_BOLD + Utility.ANSI_GREEN);
            } else {
                Utility.printlnColored("Status: CHECKED OUT ❌ (Unavailable)", Utility.ANSI_BOLD + Utility.ANSI_RED);
            }
        }
        Utility.pressEnterToContinue();
    }

    // --- TABLE FORMATTING HELPERS ---

    private void displayBooksTable(List<Book> list) {
        List<String[]> rows = new ArrayList<>();
        for (Book b : list) {
            rows.add(new String[]{
                b.getId(),
                b.getTitle(),
                b.getAuthor(),
                b.isAvailable() ? "Available" : "Issued"
            });
        }
        Utility.printTable(new String[]{"Book ID", "Title", "Author", "Status"}, rows);
    }

    private void displayIssuesTable(List<IssueRecord> list) {
        List<String[]> rows = new ArrayList<>();
        LocalDate today = LocalDate.now();
        for (IssueRecord r : list) {
            Book b = library.findBookById(r.getBookId());
            String title = (b != null) ? b.getTitle() : "Unknown Book";
            String status;
            if (r.isReturned()) {
                status = "Returned (Paid: $" + String.format("%.2f", r.getFinePaid()) + ")";
            } else {
                double fine = r.calculateFine(today);
                if (fine > 0) {
                    status = "LATE (Accumulating Fine: $" + String.format("%.2f", fine) + ")";
                } else {
                    status = "Issued (Due: " + r.getDueDate().toString() + ")";
                }
            }
            rows.add(new String[]{
                r.getIssueId(),
                r.getBookId(),
                title,
                r.getUsername(),
                r.getIssueDate().toString(),
                r.getDueDate().toString(),
                status
            });
        }
        Utility.printTable(new String[]{"Issue ID", "Book ID", "Title", "Username", "Issue Date", "Due Date", "Status"}, rows);
    }
}
