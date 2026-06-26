package com.library;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Tracks the lending status, dates, and fine calculations for issued books.
 */
public class IssueRecord implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final int LENDING_DAYS = 14;
    private static final double FINE_RATE_PER_DAY = 1.0; // $1.00 fine per day late

    private String issueId;
    private String bookId;
    private String username;
    private LocalDate issueDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private double finePaid;

    public IssueRecord(String issueId, String bookId, String username) {
        this.issueId = issueId;
        this.bookId = bookId;
        this.username = username;
        this.issueDate = LocalDate.now();
        this.dueDate = this.issueDate.plusDays(LENDING_DAYS);
        this.returnDate = null;
        this.finePaid = 0.0;
    }

    public String getIssueId() {
        return issueId;
    }

    public String getBookId() {
        return bookId;
    }

    public String getUsername() {
        return username;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public double getFinePaid() {
        return finePaid;
    }

    public void setFinePaid(double finePaid) {
        this.finePaid = finePaid;
    }

    /**
     * Calculates the overdue fine for this record.
     * If the book is returned, fine is based on the returnDate.
     * If not returned, fine is calculated dynamically up to the comparisonDate (typically today).
     */
    public double calculateFine(LocalDate comparisonDate) {
        LocalDate endPoint = (returnDate != null) ? returnDate : comparisonDate;
        if (endPoint.isAfter(dueDate)) {
            long daysLate = ChronoUnit.DAYS.between(dueDate, endPoint);
            return daysLate * FINE_RATE_PER_DAY;
        }
        return 0.0;
    }

    public boolean isReturned() {
        return returnDate != null;
    }

    @Override
    public String toString() {
        return String.format("IssueRecord [ID: %s, Book: %s, User: %s, Issued: %s, Due: %s, Returned: %s, Fine: $%.2f]",
                issueId, bookId, username, issueDate, dueDate,
                (returnDate != null ? returnDate.toString() : "Not Returned"),
                calculateFine(LocalDate.now()));
    }
}
