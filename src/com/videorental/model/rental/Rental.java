package com.videorental.model.rental;

import com.videorental.common.Identifiable;
import com.videorental.model.copy.MediaCopy;
import com.videorental.model.user.Customer;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Rental implements Identifiable {
    private final String id;
    private final Customer customer;
    private final MediaCopy copy;
    private final LocalDate startDate;
    private final LocalDate dueDate;
    private LocalDate returnDate;
    private final double dailyRate;

    public Rental(String id, Customer customer, MediaCopy copy,
                  LocalDate startDate, int days, double dailyRate) {
        this.id = id;
        this.customer = customer;
        this.copy = copy;
        this.startDate = startDate;
        this.dueDate = startDate.plusDays(days);
        this.dailyRate = dailyRate;
    }

    @Override
    public String getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public MediaCopy getCopy() {
        return copy;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public double getDailyRate() {
        return dailyRate;
    }

    public void setReturnDate(LocalDate date) {
        this.returnDate = date;
    }

    public boolean isReturned() {
        return returnDate != null;
    }

    public boolean isOverdue() {
        LocalDate checkDate = isReturned() ? returnDate : LocalDate.now();
        return checkDate.isAfter(dueDate);
    }

    public double calculateFine() {
        if (!isOverdue()) {
            return 0;
        }
        LocalDate checkDate = isReturned() ? returnDate : LocalDate.now();
        long daysLate = ChronoUnit.DAYS.between(dueDate, checkDate);
        return daysLate * dailyRate * 0.5;
    }

    @Override
    public String toString() {
        String status = isReturned()
                ? "Повернено: " + returnDate + (isOverdue() ? " [ПРОСТРОЧЕНО, штраф: " + String.format("%.2f", calculateFine()) + " грн]" : "")
                : (isOverdue() ? "ПРОСТРОЧЕНО" : "Активна");
        return String.format("[%s] %s | %s | %s — %s | %s",
                id, customer.getName(), copy.getMediaItem().getTitle(), startDate, dueDate, status);
    }
}
