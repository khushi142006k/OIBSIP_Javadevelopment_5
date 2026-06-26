package com.library;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles persistence of library data (Books, Users, Admins, IssueRecords)
 * using Java Object Serialization.
 */
public class FileManager {
    private static final String DATA_DIR = "data";
    private static final String BOOKS_FILE = DATA_DIR + "/books.ser";
    private static final String USERS_FILE = DATA_DIR + "/users.ser";
    private static final String ADMINS_FILE = DATA_DIR + "/admins.ser";
    private static final String ISSUES_FILE = DATA_DIR + "/issues.ser";

    static {
        // Ensure data directory exists
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    /**
     * Saves a list of objects to a file.
     */
    private static <T> void saveToFile(String filePath, List<T> list) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(list);
        } catch (IOException e) {
            Utility.printlnColored("Error saving data to " + filePath + ": " + e.getMessage(), Utility.ANSI_RED);
        }
    }

    /**
     * Loads a list of objects from a file. If the file doesn't exist, returns an empty ArrayList.
     */
    @SuppressWarnings("unchecked")
    private static <T> List<T> loadFromFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<T>) ois.readObject();
        } catch (FileNotFoundException e) {
            // Safe to ignore, return empty list
            return new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            Utility.printlnColored("Error reading data from " + filePath + ": " + e.getMessage(), Utility.ANSI_RED);
            return new ArrayList<>();
        }
    }

    // Book Persistence
    public static void saveBooks(List<Book> books) {
        saveToFile(BOOKS_FILE, books);
    }

    public static List<Book> loadBooks() {
        return loadFromFile(BOOKS_FILE);
    }

    // User Persistence
    public static void saveUsers(List<User> users) {
        saveToFile(USERS_FILE, users);
    }

    public static List<User> loadUsers() {
        return loadFromFile(USERS_FILE);
    }

    // Admin Persistence
    public static void saveAdmins(List<Admin> admins) {
        saveToFile(ADMINS_FILE, admins);
    }

    public static List<Admin> loadAdmins() {
        return loadFromFile(ADMINS_FILE);
    }

    // Issue Record Persistence
    public static void saveIssues(List<IssueRecord> issues) {
        saveToFile(ISSUES_FILE, issues);
    }

    public static List<IssueRecord> loadIssues() {
        return loadFromFile(ISSUES_FILE);
    }
}
