package com.pluralsight;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transactions {
    private LocalDateTime dateTime;
    private String description;
    private String vendor;
    private double amount;

    public Transactions(LocalDateTime dateTime, String description, String vendor, double amount) {
        this.dateTime = dateTime;
        this.description =description;
        this.vendor = vendor;
        this.amount= amount;
    }



    public LocalDateTime getDateTime() {
        return dateTime;
    }
    public String getDescription() {
        return description;
    }
    public String getVendor() {
        return vendor;
    }
    public double getAmount() {
        return amount;
    }

@Override
public String toString() {
    DateTimeFormatter dateFormatForFile = DateTimeFormatter.ofPattern("yyyy-MM-dd|HH:mm:ss");
    String timeFormat = String.format("%s|%s|%d|%.2f", dateTime.format(dateFormatForFile), description,vendor, amount);
    return timeFormat;
}

}

