package za.co.gabos.persistance;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * This class opens a connection with the database which allows us to persist data and read data from the diffrent tables
 */
public class Database {
    // this is the name of the db and will be stored in the root directory of the project
    private  final String dbUrl;

    public Database(String dbUrl){
        this.dbUrl = dbUrl;
    }

    /**
     * This opens a connection with the database by connecting to SQL lite
     * @return Connection, This represents a active open session between the program and the SQLite database file
     * @throws SQLException
     */
    public  Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(this.dbUrl);
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON;");
        }
        return conn;
    }

    /**
     * Reads schema.sql from src/main/resources and runs every SQL statement.
     */
    public void initializeDatabase() {
        try (InputStream is = Database.class.getClassLoader().getResourceAsStream("schema.sql")) {
            if (is == null) {
                throw new RuntimeException("Could not find schema.sql in src/main/resources bruv!");
            }

            String sql = new String(is.readAllBytes(), StandardCharsets.UTF_8);

            // Split the SQL file by semicolon so JDBC executes each statement one by one
            String[] statements = sql.split(";");

            try (Connection conn = getConnection();
                 Statement stmt = conn.createStatement()) {

                for (String statement : statements) {
                    if (!statement.trim().isEmpty()) {
                        stmt.execute(statement.trim());
                    }
                }
                System.out.println("Database tables initialized successfully.");
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize database: " + e.getMessage(), e);
        }
    }
}
