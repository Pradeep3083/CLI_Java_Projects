package org.example;

public class Account {
    private int accountId;
    private String username;
    private double balance;

    public Account(int accountId, String username, double balance){
        this.accountId =accountId;
        this.username = username;
        this.balance = balance;
    }

    public int getAccountId() { return accountId; }
    public String getUsername() { return username; }
    public double getBalance() { return balance; }

    public void setBalance(double balance) { this.balance = balance; }

}
