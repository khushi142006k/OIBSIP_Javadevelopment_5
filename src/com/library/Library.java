package com.library;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller class that manages library records, applies business logic rules,
 * and communicates with the FileManager to persist state changes.
 */
public class Library {
    private List<Book> books;
    private List<User> users;
    private List<Admin> admins;
    private List<IssueRecord> issues;

    public Library() {
        // Load data from file storage or initialize empty lists
        this.books = FileManager.loadBooks();
        this.users = FileManager.loadUsers();
        this.admins = FileManager.loadAdmins();
        this.issues = FileManager.loadIssues();

        // If admins list is empty, seed a default admin
        if (this.admins.isEmpty()) {
            this.admins.add(new Admin("admin", "admin123", "Default Admin"));
            FileManager.saveAdmins(this.admins);
        }
    }

    // --- GETTERS ---
    public List<Book> getBooks() {
        return books;
    }

    public List<User> getUsers() {
        return users;
    }

    public List<Admin> getAdmins() {
        return admins;
    }

    public List<IssueRecord> getIssues() {
        return issues;
    }

    // --- BOOK OPERATIONS ---

    /**
     * Adds a new book to the library system.
     */
    public boolean addBook(Book book) {
        if (findBookById(book.getId()) != null) {
            return false; // Book ID already exists
        }
        books.add(book);
        FileManager.saveBooks(books);
        return true;
    }

    /**
     * Updates an existing book's details.
     */
    public boolean updateBook(String id, String newTitle, String newAuthor) {
        Book book = findBookById(id);
        if (book == null) {
            return false;
        }
        if (newTitle != null && !newTitle.trim().isEmpty()) {
            book.setTitle(newTitle.trim());
        }
        if (newAuthor != null && !newAuthor.trim().isEmpty()) {
            book.setAuthor(newAuthor.trim());
        }
        FileManager.saveBooks(books);
        return true;
    }

    /**
     * Deletes a book from the library system, only if it is not currently issued.
     */
    public boolean deleteBook(String id) {
        Book book = findBookById(id);
        if (book == null) {
            return false;
        }
        // Check if book is issued
        if (!book.isAvailable()) {
            return false; // Cannot delete an issued book
        }
        books.remove(book);
        FileManager.saveBooks(books);
        return true;
    }

    /**
     * Search books by ID, Title, or Author (case-insensitive substring match).
     */
    public List<Book> searchBooks(String query) {
        List<Book> results = new ArrayList<>();
        String lowerQuery = query.toLowerCase();
        for (Book b : books) {
            if (b.getId().toLowerCase().contains(lowerQuery) ||
                b.getTitle().toLowerCase().contains(lowerQuery) ||
                b.getAuthor().toLowerCase().contains(lowerQuery)) {
                results.add(b);
            }
        }
        return results;
    }

    public Book findBookById(String id) {
        for (Book b : books) {
            if (b.getId().equalsIgnoreCase(id)) {
                return b;
            }
        }
        return null;
    }

    // --- USER OPERATIONS ---

    /**
     * Registers a new user.
     */
    public boolean registerUser(User user) {
        if (findUserByUsername(user.getUsername()) != null || findAdminByUsername(user.getUsername()) != null) {
            return false; // Username already taken
        }
        users.add(user);
        FileManager.saveUsers(users);
        return true;
    }

    /**
     * Registers a new admin.
     */
    public boolean registerAdmin(Admin admin) {
        if (findUserByUsername(admin.getUsername()) != null || findAdminByUsername(admin.getUsername()) != null) {
            return false; // Username already taken
        }
        admins.add(admin);
        FileManager.saveAdmins(admins);
        return true;
    }

    public User findUserByUsername(String username) {
        for (User u : users) {
            if (u.getUsername().equalsIgnoreCase(username)) {
                return u;
            }
        }
        return null;
    }

    public Admin findAdminByUsername(String username) {
        for (Admin a : admins) {
            if (a.getUsername().equalsIgnoreCase(username)) {
                return a;
            }
        }
        return null;
    }

    // --- ISSUE & RETURN OPERATIONS ---

    /**
     * Generates a new unique Issue ID.
     */
    private String generateIssueId() {
        int maxId = 1000;
        for (IssueRecord record : issues) {
            try {
                if (record.getIssueId().startsWith("ISS")) {
                    int idVal = Integer.parseInt(record.getIssueId().substring(3));
                    if (idVal > maxId) {
                        maxId = idVal;
                    }
                }
            } catch (NumberFormatException e) {
                // Ignore parsing errors for non-standard IDs
            }
        }
        return "ISS" + (maxId + 1);
    }

    /**
     * Issues a book to a user.
     */
    public boolean issueBook(String bookId, String username, String[] errorOut) {
        Book book = findBookById(bookId);
        if (book == null) {
            errorOut[0] = "Book does not exist.";
            return false;
        }
        if (!book.isAvailable()) {
            errorOut[0] = "Book is already issued to another user.";
            return false;
        }

        User user = findUserByUsername(username);
        if (user == null) {
            errorOut[0] = "User does not exist.";
            return false;
        }

        // Issue book
        book.setAvailable(false);
        String issueId = generateIssueId();
        IssueRecord record = new IssueRecord(issueId, bookId, username);
        issues.add(record);

        // Save records
        FileManager.saveBooks(books);
        FileManager.saveIssues(issues);
        return true;
    }

    /**
     * Returns a book and returns the late fine if any.
     */
    public double returnBook(String bookId, String[] errorOut) {
        Book book = findBookById(bookId);
        if (book == null) {
            errorOut[0] = "Book does not exist.";
            return -1.0;
        }

        // Find active issue record
        IssueRecord activeRecord = null;
        for (IssueRecord record : issues) {
            if (record.getBookId().equalsIgnoreCase(bookId) && !record.isReturned()) {
                activeRecord = record;
                break;
            }
        }

        if (activeRecord == null) {
            errorOut[0] = "This book is not currently issued according to active records.";
            return -1.0;
        }

        // Calculate fine
        LocalDate today = LocalDate.now();
        double fine = activeRecord.calculateFine(today);

        // Update record
        activeRecord.setReturnDate(today);
        activeRecord.setFinePaid(fine);
        book.setAvailable(true);

        // Save records
        FileManager.saveBooks(books);
        FileManager.saveIssues(issues);
        return fine;
    }

    /**
     * Get all active (issued but not returned) issue records.
     */
    public List<IssueRecord> getActiveIssues() {
        List<IssueRecord> active = new ArrayList<>();
        for (IssueRecord record : issues) {
            if (!record.isReturned()) {
                active.add(record);
            }
        }
        return active;
    }

    /**
     * Get active issue records for a specific user.
     */
    public List<IssueRecord> getActiveIssuesByUser(String username) {
        List<IssueRecord> userIssues = new ArrayList<>();
        for (IssueRecord record : issues) {
            if (record.getUsername().equalsIgnoreCase(username) && !record.isReturned()) {
                userIssues.add(record);
            }
        }
        return userIssues;
    }

    /**
     * Get all issue records for a specific user.
     */
    public List<IssueRecord> getAllIssuesByUser(String username) {
        List<IssueRecord> userIssues = new ArrayList<>();
        for (IssueRecord record : issues) {
            if (record.getUsername().equalsIgnoreCase(username)) {
                userIssues.add(record);
            }
        }
        return userIssues;
    }

    /**
     * Calculates current accumulated fine for a user (combining unreturned late books and paid/unpaid fines).
     */
    public double getTotalFinesByUser(String username) {
        double totalFine = 0.0;
        LocalDate today = LocalDate.now();
        for (IssueRecord record : issues) {
            if (record.getUsername().equalsIgnoreCase(username)) {
                if (record.isReturned()) {
                    totalFine += record.getFinePaid();
                } else {
                    totalFine += record.calculateFine(today);
                }
            }
        }
        return totalFine;
    }
}
