package dev.avyguzov.db;

import dev.avyguzov.ConfigsReader;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;

import static dev.avyguzov.Main.currentProfile;
import static dev.avyguzov.ConfigsReader.getPathToTheFile;

@Singleton
public class Database {
    private final ConfigsReader configsReader;

    @Inject
    public Database(ConfigsReader configsReader) throws IOException, SQLException {
        this.configsReader = configsReader;
        initDb();
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(configsReader.getProperty("db.jdbc-url"));
    }

    private void initDb() throws SQLException, IOException {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            if (conn.getMetaData().supportsBatchUpdates()) {
                stmt.addBatch(getTableInitScript());
                stmt.addBatch(getInitDataScript());
                stmt.executeBatch();
            } else {
                throw new SQLException("A DB doesn't support BatchUpdates");
            }
            conn.commit();
        }
    }

    private String getInitDataScript() throws IOException {
        var scriptFromFile = Files.readString(Path.of(getPathToTheFile("data-init-" + currentProfile.name().toLowerCase() + ".sql")));
        return scriptFromFile.replace("?", configsReader.getProperty("db.seats-table")).toString();
    }

    private String getTableInitScript() {
        return "CREATE TABLE " + configsReader.getProperty("db.seats-table") + " (\n" +
                "id integer NOT NULL,\n" +
                "is_occupied boolean,\n" +
                "version integer,\n" +
                "CONSTRAINT pk_seats PRIMARY KEY (id)\n" +
                ")";
    }
}
