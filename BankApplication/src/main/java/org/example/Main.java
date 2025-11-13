package org.example;

import java.util.List;
import java.util.Scanner;

public class Main {
    private static AccountDAO accountDAO = new AccountDAO();
    private static Account currentAccount = null;
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("--- Welcome to the CLI Bank Application ---");
        displayMainMenu();
    }

    private static void displayMainMenu(){
        while(currentAccount == null){
            System.out.println("\nSelect an option: ");
            System.out.println("1. Create New Account");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.println("Enter choice: ");

            String choice = sc.nextLine();

            switch (choice){
                case "1" -> createAccount();
                case "2" -> login();
                case "3" -> {
                    System.out.println("Thank you for using the CLI Bank Application. Goodbye!");
                    sc.close();
                    return;
                }
                default -> System.out.println("Invalid choice. Plase try again.");
            }
        }
        displayAccountMenu();
    }

    private static void displayAccountMenu(){
        while(currentAccount != null){
            System.out.printf("\n--- Welcome, %s ---%n", currentAccount.getUsername());
            System.out.println("1. Deposit");
            System.out.println("2. Withdraw");
            System.out.println("3. View Balance");
            System.out.println("4. Transaction History");
            System.out.println("5. Logout");
            System.out.print("Enter choice: ");

            String choice = sc.nextLine();

            switch (choice){
                case "1" -> handleDeposit();
                case "2" -> handleWithdrawal();
                case "3" -> viewBalance();
                case "4" -> viewTransactionHostory();
                case "5" -> {
                    currentAccount = null;
                    System.out.println("Logged out successfully");
                    displayMainMenu();
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    private static void createAccount(){
        System.out.print("Enter desired username: ");
        String username = sc.nextLine();
        System.out.print("Enter password: ");
        String password = sc.nextLine();
        System.out.print("Enter initial deposit amount (Min $50.00): $");
        double initialDeposit = 0;
        try{
            initialDeposit = Double.parseDouble(sc.nextLine());
            if(initialDeposit < 50.00){
                System.out.println("Initial deposit must be at least $50.00.");
                return;
            }
        }catch (NumberFormatException e){
            System.out.println("Invalid amount entered. Please try again");
            return;
        }
        if(accountDAO.createAccount(username, password, initialDeposit)){
            System.out.println("\nAccount created successfully! Please log in.");
        }else{
            System.out.println("Failed to create account. Please try a different username");
        }
    }

    private static void login(){
        System.out.println("Enter username: ");
        String username = sc.nextLine();
        System.out.println("Enter password: ");
        String password = sc.nextLine();

        currentAccount = accountDAO.login(username, password);

        if(currentAccount != null){
            System.out.println("\nLogin successfully");
        }else{
            System.out.println("\nInvalid username or password");
        }
    }

    private static void handleDeposit(){
        System.out.println("Enter deposit amount: $");
        try{
            double amount = Double.parseDouble(sc.nextLine());
            if(amount <= 0){
                System.err.println("Deposit amount must be positive");
                return;
            }
            if(accountDAO.deposit(currentAccount, amount)) {
                System.out.printf("Successfully deposited $%.2f. new balance: $%.2f%n", amount, currentAccount.getBalance());
            }else{
                System.out.println("Deposit failed due to a system error.");
            }
        }catch (NumberFormatException e){
            System.out.println("Invalid amount entered.");
        }
    }

    private static void handleWithdrawal(){
        System.out.println("Enter withdrawal amount: $");
        try{
            double amount = Double.parseDouble(sc.nextLine());
            if(amount <= 0){
                System.err.println("Withdrawal amount must be positive.");
                return;
            }
            if(amount > currentAccount.getBalance()){
                System.err.println("Insufficient funds. Current balance: $"+currentAccount.getBalance());
                return;
            }
            if(accountDAO.withdraw(currentAccount, amount)){
                System.out.printf("Successfully withdraw $%.2f. New balance: $%.2f%n", amount, currentAccount.getBalance());
            }else{
                System.out.println("Withdrawal failed due to a system error or insufficient funds.");
            }
        }catch (NumberFormatException e){
            System.err.println("Invalid amount entered");
        }
    }

    private static void viewBalance(){
        System.out.printf("Current Balance: $%.2f%n", currentAccount.getBalance());
    }

    private static void viewTransactionHostory(){
        List<String> history = accountDAO.getTransactionHistory(currentAccount.getAccountId());
        System.out.println("\n--- Last 10 Transactinos ---");
        if(history.isEmpty()){
            System.out.println("no transactions found.");
        }else{
            for(String transaction : history){
                System.out.println(transaction);
            }
        }
        System.out.println("----------------------------");    }

}