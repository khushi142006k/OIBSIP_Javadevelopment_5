package com.library;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Utility class containing input reading helpers, validation logic,
 * ANSI console styling, and custom table drawing utilities.
 */
public class Utility {
    private static final Scanner scanner = new Scanner(System.in);

    // ANSI Escape Codes for CLI styling
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static final String ANSI_BOLD = "\u001B[1m";
    public static final String ANSI_UNDERLINE = "\u001B[4m";

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
    );

    /**
     * Clear the console screen (ANSI support required, else fallback).
     */
    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    /**
     * Print colored text.
     */
    public static void printColored(String text, String colorCode) {
        System.out.print(colorCode + text + ANSI_RESET);
    }

    /**
     * Print colored text with a newline.
     */
    public static void printlnColored(String text, String colorCode) {
        System.out.println(colorCode + text + ANSI_RESET);
    }

    /**
     * Prints a beautiful system banner.
     */
    public static void printBanner() {
        printlnColored("==========================================================================", ANSI_CYAN);
        printlnColored(" 📚  DIGITAL LIBRARY MANAGEMENT SYSTEM  📚 ", ANSI_BOLD + ANSI_PURPLE);
        printlnColored("==========================================================================", ANSI_CYAN);
    }

    /**
     * Read a non-empty string.
     */
    public static String readString(String prompt) {
        while (true) {
            printColored(prompt, ANSI_YELLOW);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            printlnColored("Input cannot be empty. Please try again.", ANSI_RED);
        }
    }

    /**
     * Read a string that can be empty (for optional fields/updates).
     */
    public static String readOptionalString(String prompt) {
        printColored(prompt, ANSI_YELLOW);
        return scanner.nextLine().trim();
    }

    /**
     * Read a valid email address.
     */
    public static String readEmail(String prompt) {
        while (true) {
            String email = readString(prompt);
            if (EMAIL_PATTERN.matcher(email).matches()) {
                return email;
            }
            printlnColored("Invalid email format (e.g., example@domain.com). Please try again.", ANSI_RED);
        }
    }

    /**
     * Read an integer within a range.
     */
    public static int readInt(String prompt, int min, int max) {
        while (true) {
            printColored(prompt, ANSI_YELLOW);
            String input = scanner.nextLine().trim();
            try {
                int value = Integer.parseInt(input);
                if (value >= min && value <= max) {
                    return value;
                }
                printlnColored("Value out of range (" + min + " - " + max + "). Please try again.", ANSI_RED);
            } catch (NumberFormatException e) {
                printlnColored("Invalid input. Please enter a valid number.", ANSI_RED);
            }
        }
    }

    /**
     * Read a date string in YYYY-MM-DD format and parse it.
     */
    public static LocalDate readDate(String prompt) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        while (true) {
            printColored(prompt, ANSI_YELLOW);
            String input = scanner.nextLine().trim();
            try {
                return LocalDate.parse(input, formatter);
            } catch (DateTimeParseException e) {
                printlnColored("Invalid date format. Use YYYY-MM-DD (e.g., 2026-06-26).", ANSI_RED);
            }
        }
    }

    /**
     * Prints a divider line.
     */
    public static void printDivider() {
        printlnColored("--------------------------------------------------------------------------", ANSI_CYAN);
    }

    /**
     * Wait for user to press Enter to continue.
     */
    public static void pressEnterToContinue() {
        printlnColored("\nPress [Enter] to continue...", ANSI_GREEN);
        scanner.nextLine();
    }

    /**
     * Prints a nice formatted table to screen.
     */
    public static void printTable(String[] headers, List<String[]> rows) {
        if (rows == null || rows.isEmpty()) {
            printlnColored("No data to display in table.", ANSI_YELLOW);
            return;
        }

        int[] colWidths = new int[headers.length];
        for (int i = 0; i < headers.length; i++) {
            colWidths[i] = headers[i].length();
        }

        for (String[] row : rows) {
            for (int i = 0; i < row.length; i++) {
                if (row[i] != null && row[i].length() > colWidths[i]) {
                    colWidths[i] = row[i].length();
                }
            }
        }

        // Print header separator
        printHeaderSeparator(colWidths);

        // Print headers
        System.out.print("│ ");
        for (int i = 0; i < headers.length; i++) {
            String format = "%-" + colWidths[i] + "s │ ";
            printColored(String.format(format, headers[i]), ANSI_BOLD + ANSI_BLUE);
        }
        System.out.println();

        // Print mid separator
        printHeaderSeparator(colWidths);

        // Print rows
        for (String[] row : rows) {
            System.out.print("│ ");
            for (int i = 0; i < row.length; i++) {
                String format = "%-" + colWidths[i] + "s │ ";
                System.out.format(format, row[i] == null ? "" : row[i]);
            }
            System.out.println();
        }

        // Print footer separator
        printHeaderSeparator(colWidths);
    }

    private static void printHeaderSeparator(int[] widths) {
        System.out.print("├─");
        for (int i = 0; i < widths.length; i++) {
            for (int w = 0; w < widths[i]; w++) {
                System.out.print("─");
            }
            if (i == widths.length - 1) {
                System.out.print("─┤");
            } else {
                System.out.print("─┼─");
            }
        }
        System.out.println();
    }
}
