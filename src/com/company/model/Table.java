package com.company.model;

import java.time.LocalTime;

public class Table {
    private int tableNumber;
    private int tableSeats;
    private Boolean isAvailable;
    private LocalTime timeUntilCleaned;

    public Table(int tableNumber, int tableSeats) {
        this.tableNumber = tableNumber;
        this.tableSeats = tableSeats;
        isAvailable = true;
    }

    public int getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(int tableNumber) {
        this.tableNumber = tableNumber;
    }

    public int getTableSeats() {
        return tableSeats;
    }

    public void setTableSeats(int tableSeats) {
        this.tableSeats = tableSeats;
    }

    public Boolean getAvailable() {
        return isAvailable;
    }

    public void setAvailable(Boolean available) {
        isAvailable = available;
    }

    public LocalTime getTimeUntilCleaned() {
        return timeUntilCleaned;
    }

    public void setTimeUntilCleaned(LocalTime timeUntilCleaned) {
        this.timeUntilCleaned = timeUntilCleaned;
    }
}