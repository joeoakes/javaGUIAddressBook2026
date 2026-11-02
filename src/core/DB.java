package core;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Manages one embedded Apache Derby JDBC connection for the Address Book.
 * No Flyway dependency is required: JDBC creates the table on first launch.
 */
public class DB {
    // Relative path: the database folder is created in the process working directory.
    private static final String DB_URL =
            "jdbc:derby:addressbookdb;create=true;user=addressbookdb;password=addressbookdb";

    // A single connection is shared by the DAO methods in this desktop application.
    private static DB instance;
    private final Connection mConnection;

    private DB() throws SQLException {
        // Derby boots or creates the database when JDBC opens the connection.
        mConnection = DriverManager.getConnection(DB_URL);
        createContactTableIfMissing();
    }

    /** Get the singleton database helper; fail visibly if startup cannot connect. */
    public static synchronized DB getInstance() {
        if (instance == null) {
            try {
                instance = new DB();
            } catch (SQLException ex) {
                throw new IllegalStateException("Unable to initialize Derby database", ex);
            }
        }
        return instance;
    }

    /**
     * Create the CONTACT table only when it is absent.
     * Existing contacts are retained on subsequent launches.
     */
    private void createContactTableIfMissing() throws SQLException {
        DatabaseMetaData metadata = mConnection.getMetaData();
        // Unquoted Derby identifiers are stored in uppercase in system metadata.
        try (ResultSet tables = metadata.getTables(null, null, "CONTACT", new String[]{"TABLE"})) {
            if (tables.next()) {
                return;
            }
        }
        String sql = "CREATE TABLE Contact ("
                + "id INT PRIMARY KEY, "
                + "firstname VARCHAR(30) NOT NULL, "
                + "lastname VARCHAR(30) NOT NULL, "
                + "phonenumber VARCHAR(20) NOT NULL)";
        try (Statement statement = mConnection.createStatement()) {
            statement.executeUpdate(sql);
        }
    }

    /** Execute INSERT, UPDATE or DELETE; returns the number of affected rows. */
    public int executeUpdate(String sql) throws SQLException {
        try (Statement statement = mConnection.createStatement()) {
            return statement.executeUpdate(sql);
        }
    }

    /**
     * Execute a SELECT without parameters. The caller must close the ResultSet;
     * prepared statements are preferred for new queries.
     */
    public ResultSet executeQuery(String sql) throws SQLException {
        return mConnection.createStatement().executeQuery(sql);
    }

    /** Create a parameterized SQL statement to avoid SQL injection. */
    public PreparedStatement getPreparedStatement(String sql) throws SQLException {
        return mConnection.prepareStatement(sql);
    }
}
