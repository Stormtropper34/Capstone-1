package com.pluralsight;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class AccountingLedgerApp {
    private static final Scanner scanner = new Scanner(System.in);
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd|HH:mm:ss");
    public static void main(String[] args) {
        homeScreen();
        scanner.close();
    }

    public static void homeScreen() {
        while (true) {
            System.out.println("--Home Screen--");
            System.out.println("D) Add Deposit");
            System.out.println("P) Make Payment (Debit)");
            System.out.println("L) Ledger");
            System.out.println("X) Exit");
            System.out.print("Enter command: ");
            String option = scanner.nextLine().toUpperCase();

            switch (option) {
                case "D":
                    addDeposit(); break;
                case "P":
                    makePayment(); break;
                case "L":
                    ledger(); break;
                case "X":
                    System.out.println("Exiting Program"); return;
                default: System.out.println("Try again.");
            }
        }
    }

    public static void addDeposit() {
        System.out.println("\n--Deposit information--");
        System.out.print("Enter description: ");
        String description = scanner.nextLine();
        System.out.print("Enter vendor: ");
        String vendor = scanner.nextLine();
        System.out.print("Enter amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();

        LocalDateTime now = LocalDateTime.now();
        String depositRecord = String.format("%s|%s|%s|%.2f%n", now.format(DATE_TIME_FORMATTER), description, vendor, amount);

        try  {
            FileWriter fileWriter = new FileWriter("transactions.csv", true);
            BufferedWriter writer = new BufferedWriter(fileWriter);
            writer.write(depositRecord);
            System.out.println("Deposit done.");
            writer.close();
        } catch (IOException e) {
            System.err.println("Error saving: " + e.getMessage());
        }
    }

    public static void makePayment() {
        System.out.println("\n--Making a payment (Debit)--");
        System.out.print("Enter description: ");
        String description = scanner.nextLine();
        System.out.print("Enter vendor: ");
        String vendor = scanner.nextLine();
        System.out.print("Enter amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();

        LocalDateTime now = LocalDateTime.now();
        String paymentRecord = String.format("%s|%s|%s|%.2f%n", now.format(DATE_TIME_FORMATTER), description, vendor, -amount);

        try {
            FileWriter fileWriter = new FileWriter("transactions.csv", true);
            BufferedWriter writer = new BufferedWriter(fileWriter);
            writer.write(paymentRecord);
            System.out.println("Payment saved.");
            writer.close();
        } catch (IOException e) {
            System.err.println("Error payment: " + e.getMessage());
        }
    }

    public static void ledger() {
        while (true) {
            System.out.println("\n--Ledger--");
            System.out.println("A) All Entries");
            System.out.println("D) Deposits");
            System.out.println("P) Payments");
            System.out.println("R) Reports");
            System.out.println("H) Home");
            System.out.print("Enter command: ");
            String option = scanner.nextLine().toUpperCase();

            switch (option) {
                case "A":
                    displayTransactions(getAllTransactions());
                    break;
                case "D":
                    displayTransactions(getDeposits());
                    break;
                case "P":
                    displayTransactions(getPayments());
                    break;
                case "R":
                    reports();
                    break;
                case "H": return;
                default: System.out.println("Try again. :p");
            }
        }
    }

    public static List<Transactions> getAllTransactions() {
        List<Transactions> transactions = new ArrayList<>();
        try (FileReader fileReader = new FileReader("transactions.csv");
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] tokens = line.split("\\|");
                if (tokens.length == 5) {
                    String dateToken = tokens[0];
                    String timeToken = tokens[1];
                    LocalDateTime dateTime = LocalDateTime.parse(dateToken + "|" + timeToken, DATE_TIME_FORMATTER);
                    String description = tokens[2];
                    String vendor = tokens[3];
                    double amount = Double.parseDouble(tokens[4]);
                    transactions.add(new Transactions(dateTime, description, vendor, amount));

                }
            }
        } catch (IOException e) {
            System.err.println("Error reading transactions: " + e.getMessage());
        }
        return transactions;
    }

    public static List<Transactions> getDeposits() {
        List<Transactions> deposits = new ArrayList<>();
        for (Transactions t : getAllTransactions()) {
            if (t.getAmount() >= 0) deposits.add(t);{
            }
        }
        return deposits;
    }

    public static List<Transactions> getPayments() {
        List<Transactions> payments = new ArrayList<>();
        for (Transactions t : getAllTransactions()) {
            if (t.getAmount() < 0) payments.add(t);{
                payments.add(t);
            }
        }
        return payments;
    }

    public static void displayTransactions(List<Transactions> list) {
        if (list.isEmpty()) {
            System.out.println("No transactions. ;3.");
            return;
        }
        for (Transactions t : list) {
            System.out.printf("%15s | %15s | %13s | %10.2f%n",
                    t.getDateTime(), t.getDescription(), t.getVendor(), t.getAmount());
        }
    }
    public static void reports() {
        while (true) {
            System.out.println("\n--Reports--");
            System.out.println("1) Month To Date");
            System.out.println("2) Previous Month");
            System.out.println("3) Year To Date");
            System.out.println("4) Previous Year");
            System.out.println("5) Search by Vendor");
            System.out.println("0) Back to Ledger");
            System.out.print("Enter command: ");
            String option = scanner.nextLine();

            switch (option) {
                case "1":
                    displayTransactions(getMonthToDateTransactions());
                    break;
                case "2":
                    displayTransactions(getPreviousMonthTransactions());
                    break;
                case "3":
                    displayTransactions(getYearToDateTransactions());
                    break;
                case "4":
                    displayTransactions(getPreviousYearTransactions());
                    break;
                case "5":
                    System.out.print("Enter vendor name: ");
                    String vendor = scanner.nextLine();
                    displayTransactions(searchByVendor(vendor));
                    break;
                case "0": return;
                default: System.out.println("Invalid option. Try again.");
            }
        }
    }
    public static List<Transactions> getMonthToDateTransactions() {
        List<Transactions> result = new ArrayList<>();
        LocalDate today = LocalDate.now();
        for (Transactions t : getAllTransactions()) {
            if (t.getDateTime().toLocalDate().getMonth() == today.getMonth() &&
                    t.getDateTime().toLocalDate().getYear() == today.getYear()) {
                result.add(t);
            }
        }
        return result;
    }

    public static List<Transactions> getPreviousMonthTransactions() {
        List<Transactions> result = new ArrayList<>();
        LocalDate lastMonth = LocalDate.now().minusMonths(1);
        for (Transactions t : getAllTransactions()) {
            LocalDate date = t.getDateTime().toLocalDate();
            if (date.getMonth() == lastMonth.getMonth() && date.getYear() == lastMonth.getYear()) {
                result.add(t);
            }
        }
        return result;
    }

    public static List<Transactions> getYearToDateTransactions() {
        List<Transactions> result = new ArrayList<>();
        int currentYear = LocalDate.now().getYear();
        for (Transactions t : getAllTransactions()) {
            if (t.getDateTime().getYear() == currentYear) {
                result.add(t);
            }
        }
        return result;
    }

    public static List<Transactions> getPreviousYearTransactions() {
        List<Transactions> result = new ArrayList<>();
        int lastYear = LocalDate.now().minusYears(1).getYear();
        for (Transactions t : getAllTransactions()) {
            if (t.getDateTime().getYear() == lastYear) {
                result.add(t);
            }
        }
        return result;
    }

    public static List<Transactions> searchByVendor(String vendor) {
        List<Transactions> result = new ArrayList<>();
        for (Transactions t : getAllTransactions()) {
            if (t.getVendor().equalsIgnoreCase(vendor)) {
                result.add(t);
            }
        }
        return result;
    }
}

