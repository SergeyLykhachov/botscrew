package com.yahoo.slykhachov.botscrew.util;

import java.sql.ResultSet;

public interface Updatable {
    void post(ResultSet resultSet);
}
