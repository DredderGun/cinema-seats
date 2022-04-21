package dev.avyguzov.db;

import dev.avyguzov.ConfigsReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Optional;

import static dev.avyguzov.Main.currentProfile;

@Singleton
public class DatabaseDdlOperations {
    private static final Logger logger = LogManager.getLogger(DatabaseDdlOperations.class);
    protected final ConfigsReader configsReader;

    @Inject
    public DatabaseDdlOperations(ConfigsReader configsReader) throws IOException, SQLException {
        this.configsReader = configsReader;
        initDb();
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(configsReader.getProperty("db.jdbc-url"));
    }

    public void initDb() throws SQLException, IOException {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            if (conn.getMetaData().supportsBatchUpdates()) {
                stmt.addBatch(getTableInitScript());
                stmt.addBatch(getInitDataScript());
                logger.info("initDb: Starting initialize a data");
                stmt.executeBatch();
            } else {
                throw new SQLException("A DB doesn't support BatchUpdates");
            }
            conn.commit();
        }
    }

    public void clearDb() throws SQLException {
        logger.info("start: clear database");
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(getDeleteDataBaseScript());
        }
    }

    private String readFromInputStream(InputStream inputStream) throws IOException {
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br
                     = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return resultStringBuilder.toString();
    }

    private String getInitDataScript() throws IOException {
        logger.info("getInitDataScript: Start");
//        var scriptFromFile = Files.readString(Path.of(getPathToTheFile("data-init-" + currentProfile.name().toLowerCase() + ".sql")));
        var scriptFromFile = readFromInputStream(ConfigsReader.class.getClassLoader().getResourceAsStream("data-init-" + currentProfile.name().toLowerCase() + ".sql"));
        return scriptFromFile.replace("?", configsReader.getProperty("db.seats-table")).toString();
    }

    private String getTableInitScript() {
        logger.info("getTableInitScript: Start");
        logger.info("Current table is {} ", configsReader.getProperty("db.seats-table"));
        return "CREATE TABLE " + configsReader.getProperty("db.seats-table") + " (\n" +
                "id integer NOT NULL,\n" +
                "is_occupied boolean,\n" +
                "version integer,\n" +
                "CONSTRAINT pk_seats PRIMARY KEY (id)\n" +
                ")";
    }

    private String getDeleteDataBaseScript() {
        logger.info("getDeleteDataBaseScript: Start");
        return "DROP TABLE " + configsReader.getProperty("db.seats-table");
    }
}
