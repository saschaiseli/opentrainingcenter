package ch.opentrainingcenter.db.postgres;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.junit.Ignore;
import org.junit.Test;
import org.postgresql.Driver;

import ch.opentrainingcenter.core.db.DatabaseConnectionConfiguration;
import ch.opentrainingcenter.db.USAGE;
import ch.opentrainingcenter.db.internal.Dao;

public class TestCreate {

    private DatabaseAccessPostgres access;

    @Test
    @Ignore
    public void test1() {
        final String url = "jdbc:postgresql://localhost/postgres";
        final Properties props = new Properties();
        props.setProperty("user", "otc_user");
        props.setProperty("password", "otc_user");
        final Driver driver;
        try {
            final Connection conn = DriverManager.getConnection(url, props);
            final Statement statement = conn.createStatement();
            statement.execute("create database my_db");
        } catch (final SQLException e) {
            e.printStackTrace();
        }

        final DatabaseConnectionConfiguration config = new DatabaseConnectionConfiguration("org.postgresql.Driver", "jdbc:postgresql://localhost/my_db",
                "otc_user", "otc_user", "org.hibernate.dialect.PostgreSQLDialect");
        access = new DatabaseAccessPostgres(new Dao(USAGE.TEST, config));
        access.createDatabase();
    }
}
