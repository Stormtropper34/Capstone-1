package com.pluralsight;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AccountingLedgerApp {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        homeScreen();
        scanner.close();
    }
        public static void homeScreen() {
            while (true) {
                System.out.println("--Home Screen--");
                System.out.println("----------------");
                System.out.println("D) Add Deposit");
                System.out.println("P) Make Payment (Debit)");
                System.out.println("L) Ledger");
                System.out.println("X) Exit");
                System.out.println("----------------");
                System.out.println("Enter command: ");
                String option = scanner.nextLine().toUpperCase();

                switch (option) {
                    case "D":
                        addDeposit();
                        break;
                    case "P":
                        makePayment();
                        break;
                    case "L":
                        ledger();
                        break;
                    case "X":
                        System.out.println("Exiting Program");
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Invalid option. Try again");
                }
            }
        }

    public static void addDeposit() {
        System.out.println("\n--Deposit information-- ");
        System.out.println("Enter date (yyyy-MM-dd): ");
        String date = scanner.nextLine();
        System.out.println("Enter time (HH:mm:ss): ");
        String time = scanner.nextLine();
        System.out.println("Enter description: ");
        String description = scanner.nextLine();
        System.out.println("Enter vendor: ");
        String vendor = scanner.nextLine();
        System.out.println("Enter amount: ");
        double amount = scanner.nextDouble();
        String depositStr = String.format("%s|%s|%s|%s|%.2f%n", date, time, description, vendor, amount);

            try {
                FileWriter fileWriter = new FileWriter("transactions.csv", true);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                bufferedWriter.write("\n");
                bufferedWriter.write(depositStr);
                bufferedWriter.close();
            } catch (IOException e) {
                System.out.println("Error. Try again.");
                e.getStackTrace();
            }

    }

    public static void makePayment() {
        System.out.println("\n--Making a payment(Debit)--");
        System.out.print("Enter date (yyyy-MM-dd): ");
        String date = scanner.nextLine();
        System.out.print("Enter time (HH:mm:ss): ");
        String time = scanner.nextLine();
        System.out.print("Enter your payment description: ");
        String description = scanner.nextLine();
        System.out.print("Enter vendor: ");
        String vendor = scanner.nextLine();
        System.out.print("Enter amount: ");
        double amount = scanner.nextDouble();
        String depositStr = String.format("%s|%s|%s|%s|%.2f%n", date, time, description, vendor, amount);

            try {
                FileWriter fileWriter = new FileWriter("transactions.csv", true);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                bufferedWriter.write("\n");
                bufferedWriter.write(depositStr);
                bufferedWriter.close();
            } catch (IOException e) {
                System.out.println("Please try again.");
                e.getStackTrace();
            }
    }

    public static void ledger() {
        while (true) {
            System.out.println("\n--Ledger--");
            System.out.println("A) All- all entries");
            System.out.println("D) Deposits");
            System.out.println("P) Payments");
            System.out.println("R) Reports");
            System.out.println("-------------");
            System.out.println("Enter command: ");
            String option = scanner.nextLine().toUpperCase();

            switch (option) {
                case "A":
                    getAllTransactions();
                    break;
                case "D":
                   getDeposits();
                    break;
                case "P":
                    getPayments();
                    break;
                case "R":
                    reports();
                    break;
                case "H":
                    return;
                default:
                    System.out.println("Try again :p");


            }

        }

    }
    private static ArrayList<Transactions> getAllTransactions(){
        ArrayList<Transactions> transactions = new ArrayList<>();
        try  {
            FileReader fileReader = new FileReader("transactions.csv");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
//            bufferedReader.readLine();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] tokens = line.split("\\|");
                if (tokens.length == 5) {
                    try {
                        System.out.printf("%-10s | %-8s | %-10s | %-10s | %5s%n",
                                tokens[0], tokens[1], tokens[2], tokens[3], tokens[4]);
                    } catch (NumberFormatException e) {
                        System.out.println("Wrong format");
                    }
                }else{
                    System.err.println(" ");
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            e.getStackTrace();
        }
        return transactions;
    }

    private static ArrayList<Transactions> getDeposits() {
        ArrayList<Transactions> transactions = getAllTransactions();
        ArrayList<Transactions> deposits = new ArrayList<>();
        for (Transactions transaction : transactions) {
            if (transaction.getAmount() >= 0) {
                deposits.add(transaction);
            }
        }
        return transactions;

    }

    private static ArrayList<Transactions> getPayments() {
        ArrayList<Transactions> transactions = getAllTransactions();
        ArrayList<Transactions> payments = new ArrayList<>();
        for (Transactions transaction : transactions) {
            if (transaction.getAmount() < 0) {
                payments.add(transaction);
            }
        }
        return transactions;
    }

    private static void reports() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n--Reports--");
            System.out.println("1) Month To Date ");
            System.out.println("2) Previous Month ");
            System.out.println("3) Year To Date ");
            System.out.println("4) Previous Year ");
            System.out.println("5) Search by Vendor ");
            System.out.println("0) Back - Go back to Report page");
            System.out.print("Enter command: ");
            String option = scanner.nextLine();

            switch (option) {
                case "1":
                    MonthToDateTransactions();
                    break;
                case "2":
                    PreviousMonthTransactions();
                    break;
                case "3":
                    YearToDateTransactions();
                    break;
                case "4":
                    PreviousYearTransactions();
                    break;
                case "5":
                    System.out.print("Enter vendor name to search: ");
                    String vendor = scanner.nextLine();
                    searchByVendor(vendor);
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Try again.");
            }
        }
    }

    private static ArrayList<Transactions> MonthToDateTransactions() {
        ArrayList<Transactions> transactions = getAllTransactions();
        ArrayList<Transactions> monthToDate = new ArrayList<>();
        LocalDate today = LocalDate.now();
        for (Transactions transaction : transactions) {
            if (transaction.getDateTime().toLocalDate().getYear() == today.getYear() && transaction.getDateTime().toLocalDate().getMonth() == today.getMonth()) {
                monthToDate.add(transaction);
            }
        }
        return monthToDate;
    }

    private static ArrayList<Transactions> PreviousMonthTransactions() {
        ArrayList<Transactions> transactions = getAllTransactions();
        ArrayList<Transactions> currentMonth = new ArrayList<>();
        LocalDate today = LocalDate.now();
        LocalDate previousMonth = today.minusMonths(1);
        for (Transactions transaction : transactions) {
            if (transaction.getDateTime().getYear() == previousMonth.getYear() && transaction.getDateTime().getMonth() == previousMonth.getMonth()) {
                currentMonth.add(transaction);
            }
        }
        return currentMonth;
    }

    private static ArrayList<Transactions> YearToDateTransactions() {
        ArrayList<Transactions> transactions = getAllTransactions();
        ArrayList<Transactions> yearToDate = new ArrayList<>();
        LocalDate today = LocalDate.now();
        for (Transactions transaction : transactions) {
            if (transaction.getDateTime().toLocalDate().getYear() == today.getYear()) {
                yearToDate.add(transaction);
            }
        }
        return yearToDate;
    }

    private static ArrayList<Transactions> PreviousYearTransactions() {
        ArrayList<Transactions> transactions = getAllTransactions();
        ArrayList<Transactions> previousYear = new ArrayList<>();
        LocalDate today = LocalDate.now();
        for (Transactions transaction : transactions) {
            if (transaction.getDateTime().toLocalDate().getYear() == today.minusYears(1).getYear()) {
                previousYear.add(transaction);
            }
        }
        return previousYear;
    }

    private static List<Transactions> searchByVendor(String vendor) {
        List<Transactions> transactions = getAllTransactions();
        List<Transactions> searchVendor = new ArrayList<>();
        for (Transactions transaction : transactions) {
            if (transaction.getVendor().equalsIgnoreCase(vendor)) {
                searchVendor.add(transaction);
            }
        }
        return searchVendor;
    }
}





