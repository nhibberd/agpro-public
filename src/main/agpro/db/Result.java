package agpro.db;

import agpro.data.core.Function;
import agpro.data.error.ServerException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Result {
    private PreparedStatement statement;

    public <A> A result(PreparedStatement statement, Function<ResultSet, A> f) {
        this.statement = statement;
        ResultSet c = getresult();
        try {
            return f.apply(c);
        } catch (Exception e) {
            rollback(c);
            throw new ServerException(e);
        } finally {
            close(c);
        }
    }

    private Boolean next(ResultSet c) {
        try {
            return c.next();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    private ResultSet getresult() {
        try {
            return statement.executeQuery();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    private void close(ResultSet c) {
        try {
            c.close();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    private void rollback(ResultSet c) {
        try {
            c.close();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }
}