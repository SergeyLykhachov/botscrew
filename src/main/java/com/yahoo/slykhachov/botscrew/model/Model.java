package com.yahoo.slykhachov.botscrew.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yahoo.slykhachov.botscrew.util.Subject;
import com.yahoo.slykhachov.botscrew.util.Observer;
import com.yahoo.slykhachov.botscrew.repository.DBPool;
import org.springframework.stereotype.Component;

@Component
public class Model implements Subject {
    private static final Logger LOGGER = LoggerFactory.getLogger(Model.class);
    private final DBPool dataSource;
    private Observer observer;
    private ResultSet resultSet;

    public Model(DBPool hikariDataSource) {
        this.dataSource = hikariDataSource;
    }

    public void executeQuery(String sql, String... params) {
        try (
            Connection con = this.dataSource.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            Stream.iterate(0, n  ->  n  + 1)
                .limit(params.length)
                .forEach(
                    i -> {
                        try {
                            ps.setString(i + 1, params[i]);
                        } catch (SQLException e) {
                            LOGGER.error(e.getMessage(), e);
                        }
                    });
            LOGGER.info(extractSqlFromPreparedStatement(ps));
            this.resultSet = ps.executeQuery();
            this.notifyObserver();
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            try {
                if (this.resultSet != null) {
                    resultSet.close();
                }
            } catch (SQLException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
    }

    @Override
    public void registerObserver(Observer o) {
        this.observer = o;
    }

    @Override
    public void notifyObserver() {
        this.observer.update(this.resultSet);
    }

    private static String extractSqlFromPreparedStatement(PreparedStatement ps) {
        return Stream.of(ps.toString().split("[:]"))
            .skip(1)
            .collect(java.util.stream.Collectors.joining(":"));
    }

}

