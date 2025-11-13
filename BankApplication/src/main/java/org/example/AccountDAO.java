package org.example;

import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO {

    public boolean createAccount(String username, String password, double initialDeposit){
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        String sql = "INSERT INTO Accounts (username, password, balance) VALUES (?,?,?)";

        try(Connection con = DBConnection.getConnection()){
            PreparedStatement pstmt = con.prepareStatement(sql);

            pstmt.setString(1,username);
            pstmt.setString(2, hashedPassword);
            pstmt.setDouble(3, initialDeposit);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            if(e.getSQLState().equals("23000")){
                System.err.println("Error: The username '"+username+"' already exists.");
            }else{
                e.printStackTrace();
            }
            return false;
        }
    }

    public Account login(String username, String password){
        String sql = "SELECT account_id, password, balance from accounts where username = ?";
        Account account = null;

        try(Connection con = DBConnection.getConnection();
            PreparedStatement pstmt = con.prepareStatement(sql)){
            pstmt.setString(1, username);
            try(ResultSet rs = pstmt.executeQuery()){
                if(rs.next()){
                    String storedHash = rs.getString("password");

                    if(BCrypt.checkpw(password, storedHash)){
                        int accountId = rs.getInt("account_id");
                        double balance = rs.getDouble("balance");
                        account = new Account(accountId, username, balance);
                    }
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return account;
    }

    public boolean deposit(Account account, double amount){
        Connection conn = null;
        try{
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            String updateSql = "UPDATE Accounts SET balance = balance + ? WHERE account_id = ?";
            try(PreparedStatement updateStmt = conn.prepareStatement(updateSql)){
                updateStmt.setDouble(1, amount);
                updateStmt.setInt(2, account.getAccountId());
                updateStmt.executeUpdate();
            }

            String logSql = "INSERT INTO Transactions (account_id, type, amount) VALUES (?,?,?)";
            try(PreparedStatement logStmt = conn.prepareStatement(logSql)){
                logStmt.setInt(1, account.getAccountId());
                logStmt.setString(2, "Deposit");
                logStmt.setDouble(3, amount);
                logStmt.executeUpdate();
            }

            conn.commit();
            account.setBalance(account.getBalance() + amount);
            return true;
        } catch (SQLException e){
            if(conn != null){
                try{
                    conn.rollback();
                }catch (SQLException ex){
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
            return false;
        } finally {
            if(conn != null){
                try{
                    conn.setAutoCommit(true);
                    conn.close();
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
        }
    }

    public boolean withdraw(Account account, double amount){
        if(account.getBalance() < amount){
            System.err.println("Error: Insufficient funds.");
            return false;
        }
        Connection conn = null;
        try{
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            String updateSql = "UPDATE Accounts SET balance = balance - ? WHERE account_id = ?";
            try(PreparedStatement updateStmt = conn.prepareStatement(updateSql)){
                updateStmt.setDouble(1, amount);
                updateStmt.setInt(2, account.getAccountId());
                updateStmt.executeUpdate();
            }

            String logSql = "INSERT INTO Transactions (account_id, type, amount) VALUES (?,?,?)";
            try(PreparedStatement logStmt = conn.prepareStatement(logSql)){
                logStmt.setInt(1, account.getAccountId());
                logStmt.setString(2, "Withdrawal");
                logStmt.setDouble(3, amount);
                logStmt.executeUpdate();
            }
            conn.commit();
            account.setBalance(account.getBalance() - amount);
            return true;
        } catch (SQLException e){
            if(conn != null){
                try{
                    conn.rollback();
                }catch (SQLException ex){
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
            return false;
        }
        finally{
            if(conn != null){
                try{
                    conn.setAutoCommit(true);
                    conn.close();
                }
                catch (SQLException e){
                    e.printStackTrace();
                }
            }
        }
    }

    public List<String> getTransactionHistory(int accountId){
        List<String> history = new ArrayList<>();

        String sql = "SELECT type, amount, transaction_time FROM Transactions WHERE account_id = ? ORDER BY transaction_time DESC LIMIT 10";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setInt(1,accountId);
            try(ResultSet rs = pstmt.executeQuery()){
                while(rs.next()){
                    String type = rs.getString("type");
                    double amount = rs.getDouble("amount");
                    String time = rs.getTimestamp("transaction_time").toString();

                    history.add(String.format("%s: $%.2f at %s", type, amount, time));
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return history;
    }

}
