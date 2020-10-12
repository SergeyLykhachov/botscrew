package com.yahoo.slykhachov.botscrew.util;

import java.sql.ResultSet;

public interface Observer {
    void update(ResultSet resultSet);
}
