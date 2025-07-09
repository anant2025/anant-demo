package org.example.utils;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class DatabaseUtils {
    private static DatabaseUtils instance;
    private Properties dbProperties;
    private Connection connection;
    private String currentDatabaseType;

    private DatabaseUtils() {
        loadDatabaseProperties();
    }

    private void loadDatabaseProperties() {
        dbProperties = new Properties();
        try {
            dbProperties.load(getClass().getResourceAsStream("/database.properties"));
        } catch (Exception e) {
            throw new RuntimeException("Error loading database properties: " + e.getMessage());
        }
    }

    public static DatabaseUtils getInstance() {
        if (instance == null) {
            instance = new DatabaseUtils();
        }
        return instance;
    }

    public void connectToDatabase(String databaseType) throws SQLException {
        currentDatabaseType = databaseType.toLowerCase();
        String url = getJdbcUrl();
        String user = getProperty(databaseType + ".username");
        String password = getProperty(databaseType + ".password");
        
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
        
        connection = DriverManager.getConnection(url, user, password);
    }

    private String getJdbcUrl() {
        switch (currentDatabaseType) {
            case "mysql":
                return "jdbc:mysql://" + getProperty("mysql.host") + ":" + getProperty("mysql.port") + "/" + getProperty("mysql.database");
            case "postgresql":
                return "jdbc:postgresql://" + getProperty("postgresql.host") + ":" + getProperty("postgresql.port") + "/" + getProperty("postgresql.database");
            case "oracle":
                return "jdbc:oracle:thin:@" + getProperty("oracle.host") + ":" + getProperty("oracle.port") + ":" + getProperty("oracle.sid");
            default:
                throw new IllegalArgumentException("Unsupported database type: " + currentDatabaseType);
        }
    }

    private String getProperty(String key) {
        return dbProperties.getProperty(key);
    }

    public ResultSet executeQuery(String query) throws SQLException {
        if (connection == null || connection.isClosed()) {
            throw new IllegalStateException("Database connection is not established");
        }
        
        PreparedStatement stmt = connection.prepareStatement(query);
        return stmt.executeQuery();
    }

    public void executeUpdate(String query) throws SQLException {
        if (connection == null || connection.isClosed()) {
            throw new IllegalStateException("Database connection is not established");
        }
        
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.executeUpdate();
    }

    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void insertData(String tableName, Map<String, Object> data) throws SQLException {
        StringBuilder columns = new StringBuilder();
        StringBuilder values = new StringBuilder();
        
        for (String column : data.keySet()) {
            if (columns.length() > 0) {
                columns.append(", ");
                values.append(", ");
            }
            columns.append(column);
            values.append("?");
        }
        
        String query = "INSERT INTO " + tableName + " (" + columns.toString() + ") VALUES (" + values.toString() + ")";
        PreparedStatement stmt = connection.prepareStatement(query);
        
        int index = 1;
        for (Object value : data.values()) {
            if (value instanceof String) {
                stmt.setString(index, (String) value);
            } else if (value instanceof Integer) {
                stmt.setInt(index, (Integer) value);
            } else if (value instanceof Double) {
                stmt.setDouble(index, (Double) value);
            } else if (value instanceof Boolean) {
                stmt.setBoolean(index, (Boolean) value);
            }
            index++;
        }
        
        stmt.executeUpdate();
    }

    public void updateData(String tableName, Map<String, Object> data, String whereClause) throws SQLException {
        StringBuilder setClause = new StringBuilder();
        
        for (String column : data.keySet()) {
            if (setClause.length() > 0) {
                setClause.append(", ");
            }
            setClause.append(column).append(" = ?");
        }
        
        String query = "UPDATE " + tableName + " SET " + setClause.toString() + " WHERE " + whereClause;
        PreparedStatement stmt = connection.prepareStatement(query);
        
        int index = 1;
        for (Object value : data.values()) {
            if (value instanceof String) {
                stmt.setString(index, (String) value);
            } else if (value instanceof Integer) {
                stmt.setInt(index, (Integer) value);
            } else if (value instanceof Double) {
                stmt.setDouble(index, (Double) value);
            } else if (value instanceof Boolean) {
                stmt.setBoolean(index, (Boolean) value);
            }
            index++;
        }
        
        stmt.executeUpdate();
    }

    public void deleteData(String tableName, String whereClause) throws SQLException {
        String query = "DELETE FROM " + tableName + " WHERE " + whereClause;
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.executeUpdate();
    }

    public void printAllData(String tableName) throws SQLException {
        ResultSet rs = executeQuery("SELECT * FROM " + tableName);
        ResultSetMetaData metaData = rs.getMetaData();
        
        // Print column headers
        for (int i = 1; i <= metaData.getColumnCount(); i++) {
            System.out.print(metaData.getColumnName(i) + "\t");
        }
        System.out.println();
        
        // Print data
        while (rs.next()) {
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                System.out.print(rs.getString(i) + "\t");
            }
            System.out.println();
        }
    }

    public void testConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            throw new IllegalStateException("Database connection is not established");
        }
        
        if (connection.isValid(5)) {
            System.out.println("Database connection is valid");
        } else {
            throw new SQLException("Database connection is invalid");
        }
    }

    public static void main(String[] args) {
        try {
            DatabaseUtils dbUtils = DatabaseUtils.getInstance();
            
            // Connect to MySQL database
            dbUtils.connectToDatabase("mysql");
            
            // Test connection
            dbUtils.testConnection();
            
            // Example usage
            ResultSet rs = dbUtils.executeQuery("SELECT * FROM users LIMIT 1");
            while (rs.next()) {
                System.out.println("User ID: " + rs.getInt("id"));
            }
            
            // Close connection
            dbUtils.closeConnection();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
