package com.example.GymProject.actuator;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Component
public class CustomDatabaseHealthIndicator implements HealthIndicator {

    private final DataSource dataSource;

    public CustomDatabaseHealthIndicator(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Health health() {
        try (Connection connection = dataSource.getConnection()) {
            if (connection.isValid(1)) {
                return Health.up().withDetail("database", "Database is up and running").build();
            } else {
                return Health.down().withDetail("database", "Database connection is not valid").build();
            }
        } catch (SQLException e) {
            return Health.down(e).withDetail("database", "Database connection failed").build();
        }
    }
}