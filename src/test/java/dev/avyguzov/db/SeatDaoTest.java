package dev.avyguzov.db;

import org.junit.BeforeClass;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

import javax.sql.DataSource;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.LogManager;

import static org.junit.Assert.assertEquals;

public class SeatDaoTest {
    static {
        // Postgres JDBC driver uses JUL; disable it to avoid annoying, irrelevant, stderr logs during connection testing
        LogManager.getLogManager().getLogger("").setLevel(Level.OFF);
    }

    private Connection underTest;

    @Container
    public static GenericContainer postgres = new GenericContainer(DockerImageName.parse("postgres:10"))
            .withExposedPorts(6379);

    @BeforeClass
    public static void setUp() {
//        Construct the connection URL
//        String dbURL = "jdbc:tc:postgresql:9.6.8:///testdatabase?" +
//                "TC_INITFUNCTION=dev.avyguzov.db.SeatDaoTest::initDbFunction";
//        String userId = "seats";
//        String password = "seats";

        // Get a connection
//        Connection conn = DriverManager.getConnection(dbURL, userId, password);
//
//        // Set the auto-commit off
//        conn.setAutoCommit(false);
//
//        return conn;

        postgres.start();


    }

    public static void initDbFunction(Connection connection) throws SQLException {

    }

//    @Test
//    public void testSimple() throws SQLException {
//        try (PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(POSTGRES_TEST_IMAGE)) {
//            postgres.start();
//
//            ResultSet resultSet = performQuery(postgres, "SELECT 1");
//            int resultSetInt = resultSet.getInt(1);
//            assertEquals("A basic SELECT query succeeds", 1, resultSetInt);
//        }
//    }
//
//    protected ResultSet performQuery(JdbcDatabaseContainer<?> container, String sql) throws SQLException {
//        DataSource ds = getDataSource(container);
//        Statement statement = ds.getConnection().createStatement();
//        statement.execute(sql);
//        ResultSet resultSet = statement.getResultSet();
//
//        resultSet.next();
//        return resultSet;
//    }
//
//    protected DataSource getDataSource(JdbcDatabaseContainer<?> container) {
//        HikariConfig hikariConfig = new HikariConfig();
//        hikariConfig.setJdbcUrl(container.getJdbcUrl());
//        hikariConfig.setUsername(container.getUsername());
//        hikariConfig.setPassword(container.getPassword());
//        hikariConfig.setDriverClassName(container.getDriverClassName());
//
//        return new HikariDataSource(hikariConfig);
//    }
}
