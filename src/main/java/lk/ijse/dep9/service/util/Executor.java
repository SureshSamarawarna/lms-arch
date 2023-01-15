package lk.ijse.dep9.service.util;

import java.sql.SQLException;

@FunctionalInterface
public interface Executor {
    void executionContext() throws SQLException;

    static void execute(Executor e){
        try {
            e.executionContext();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
}
