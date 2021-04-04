package dev.avyguzov.db;

import dev.avyguzov.service.ConfigReader;

import javax.inject.Inject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;

import static dev.avyguzov.Main.currentProfile;
import static dev.avyguzov.service.ConfigReader.getPathToTheFile;

public class Database {
    private final ConfigReader configReader;

    @Inject
    public Database(ConfigReader configReader) {
        this.configReader = configReader;
    }

    public Connection getConnection() throws SQLException {
        Driver driver = new org.postgresql.Driver();
        DriverManager.registerDriver(driver);

        var dbURL = configReader.getProperty("db.url");
        var userId = configReader.getProperty("db.user");
        var password = configReader.getProperty("db.password");

        Connection conn = DriverManager.getConnection(dbURL, userId, password);
        conn.setAutoCommit(false);

        return conn;
    }

    public static void initDb(Database database) throws SQLException, IOException {
        var schemaInitSql = Files.readString(Path.of(getPathToTheFile("schema-init-" + currentProfile.name().toLowerCase() + ".sql")));
        var dataInitSql = Files.readString(Path.of(getPathToTheFile("data-init-" + currentProfile.name().toLowerCase() + ".sql")));

        try (Connection conn = database.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(schemaInitSql);
            stmt.executeUpdate(dataInitSql);
            conn.commit();
        }
    }

}
