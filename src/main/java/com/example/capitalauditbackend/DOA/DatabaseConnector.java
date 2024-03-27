package com.example.capitalauditbackend.DOA;
import com.example.capitalauditbackend.model.PaymentData;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class DatabaseConnector {

    private static Connection connection;
    private static DatabaseConnector instance;
    public void connect() {

        try {

            String url = "jdbc:sqlite:C:/Users/benan/IdeaProjects/CapitalAuditBackend/database.db";
            connection = DriverManager.getConnection(url);

            System.out.println("Connection to SQLite has been established.");
            setConnection(connection);
        }
        catch(SQLException e)
        {
                System.out.println(e.getMessage());
        }
    }
    public static DatabaseConnector getInstance() {
        if (instance == null) {
            instance = new DatabaseConnector();
            instance.connect();
        }
        return instance;
    }

    public boolean ExecuteLoginQuery(String username, String password)
    {
        System.out.println("Executing login query");
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            // Bind parameters
            statement.setString(1, username);
            statement.setString(2, password);

            // Execute query
            try (ResultSet resultSet = statement.executeQuery()) {
                // Check if the result set has any rows (user found)
                System.out.println("Success");
                return resultSet.next();
            }
        } catch (SQLException e) {
            // Handle exceptions
            e.printStackTrace();
            return false;
        }
    }

    public boolean PostPaymentData(int price, String category, boolean debit_credit, boolean cleared, String date, int user_id)
    {
        String query = "INSERT INTO transactions (price, category, debit_credit, cleared, date, user_id) VALUES (?, ?, ?, ?, ?, ?)";
        connect();
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            System.out.println("binding parameters");
            // Bind parameters


            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date parsedDate = sdf.parse(date); // Parse the input string into a java.util.Date object
            java.sql.Date sqlDate = new java.sql.Date(parsedDate.getTime());
            System.out.println(sqlDate);
            System.out.println(sqlDate.toString());
            System.out.println(sqlDate.toLocalDate());
// Now you can use sqlDate in your PreparedStatement
            statement.setInt(1, price);
            statement.setString(2, category);
            statement.setBoolean(3, debit_credit);
            statement.setBoolean(4, cleared);
            statement.setString(5, date); // Assuming date is in "yyyy-MM-dd" format
            statement.setInt(6, user_id);

            // Execute query
            int rowsInserted = statement.executeUpdate();
            if(rowsInserted > 0)
            {
                System.out.println("success added data.");
                return true;
            }
            else
            {
                System.out.println("failed to add data.");
                return false;
            }
        } catch (SQLException e) {
            // Handle exceptions
            System.out.println("failed" + e);
            return false;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public List<PaymentData> getPaymentData(int user_id)
    {
        String query = "SELECT * FROM transactions WHERE user_id = ?;";
        connect();
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            // Bind parameters
            statement.setInt(1, user_id);

            // Execute query
            try (ResultSet resultSet = statement.executeQuery()) {
                List<PaymentData> paymentDataList= new ArrayList<>();
                while (resultSet.next()) {
                    PaymentData paymentData = new PaymentData();
                    paymentData.setID(resultSet.getInt("ID"));
                    paymentData.setPrice(resultSet.getInt("price"));
                    paymentData.setCategory(resultSet.getString("category"));
                    paymentData.setDebit_credit(resultSet.getBoolean("debit_credit"));
                    paymentData.setCleared(resultSet.getBoolean("cleared"));
                    paymentData.setDate(resultSet.getString("date"));
                    paymentDataList.add(paymentData);

                }
                return paymentDataList;
            }
        } catch (SQLException e) {
            // Handle exceptions
            return null;
        }
    }

    public boolean checkUsername(String username)
    {
        String query = "SELECT * FROM users WHERE username = ?";
        connect();
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            // Bind parameters
            statement.setString(1, username);
            System.out.println(statement);
            // Execute query
            try (ResultSet resultSet = statement.executeQuery()) {
                // Check if the result set has any rows (user found)
                return resultSet.next();
            }
        } catch (SQLException e) {
            // Handle exceptions
            System.out.println(e);
            return false;
        }
    }

    public int getUserId(String username)
    {
        String query = "SELECT user_id FROM users WHERE username = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            // Bind parameters
            statement.setString(1, username);

            // Execute query
            try (ResultSet resultSet = statement.executeQuery()) {
                // Check if the result set has any rows (user found)
                if(resultSet.next())
                {
                    return resultSet.getInt("user_id");
                }
            }
        } catch (SQLException e) {
            // Handle exceptions
            return 1;
        }
        return 1;
    }

    public static Connection getConnection()
    {
        return DatabaseConnector.connection;
    }

    public void setConnection(Connection connection) {
        DatabaseConnector.connection = connection;
    }
}
