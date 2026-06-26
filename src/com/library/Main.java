package com.library;

/**
 * Main application entry point for the Digital Library Management System.
 */
public class Main {
    public static void main(String[] args) {
        try {
            // Instantiate library services and load persistent databases
            Library library = new Library();
            Authentication auth = new Authentication();

            // Fire up console interface
            Menu menu = new Menu(library, auth);
            menu.start();
        } catch (Exception e) {
            System.err.println("Fatal application error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
