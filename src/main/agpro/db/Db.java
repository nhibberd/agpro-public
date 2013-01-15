package agpro.db;

import agpro.data.core.Action;
import agpro.data.core.Function;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class Db {
    private final Statement statement = new Statement();

    public void executeUpdate(Connection connection, String data) {
        statement.withStatement(connection, data, new Action<PreparedStatement>() {
            public void apply(PreparedStatement preparedStatement) {
                EdgePreparedStatement z = new EdgePreparedStatement(preparedStatement);
                z.executeUpdate();
            }
        });
    }


    public void executeUpdateOneInt(Connection connection, String data, final Integer integer) {
        statement.withStatement(connection, data, new Action<PreparedStatement>() {
            public void apply(PreparedStatement preparedStatement) {
                EdgePreparedStatement z = new EdgePreparedStatement(preparedStatement);
                z.setInt(1,integer);
                z.executeUpdate();
            }
        });
    }

    public Boolean queryExists(Connection connection, String data) {
        return statement.withStatement(connection, data, new Function<PreparedStatement, Boolean>() {
            public Boolean apply(PreparedStatement preparedStatement) {
                EdgePreparedStatement z = new EdgePreparedStatement(preparedStatement);
                EdgeResultSet resultSet = new EdgeResultSet(z);
                return resultSet.next();
            }
        });
    }

    public Integer queryInt(Connection connection, String data, final Integer Column) {
        return statement.withStatement(connection, data, new Function<PreparedStatement, Integer>() {
            public Integer apply(PreparedStatement preparedStatement) {
                EdgePreparedStatement z = new EdgePreparedStatement(preparedStatement);
                EdgeResultSet resultSet = new EdgeResultSet(z);
                if(resultSet.next())
                    return resultSet.getInt(Column);
                return null;
            }
        });
    }

    public String queryString(Connection connection, String data, final Integer Column) {
        return statement.withStatement(connection, data, new Function<PreparedStatement, String>() {
            public String apply(PreparedStatement preparedStatement) {
                EdgePreparedStatement z = new EdgePreparedStatement(preparedStatement);
                EdgeResultSet resultSet = new EdgeResultSet(z);
                if(resultSet.next())
                    return resultSet.getString(Column);
                return null;
            }
        });
    }

    public Boolean queryBool(Connection connection, String data, final Integer Column) {
        return statement.withStatement(connection, data, new Function<PreparedStatement, Boolean>() {
            public Boolean apply(PreparedStatement preparedStatement) {
                EdgePreparedStatement z = new EdgePreparedStatement(preparedStatement);
                EdgeResultSet resultSet = new EdgeResultSet(z);
                if(resultSet.next())
                    return resultSet.getBoolean(Column);
                return null;
            }
        });
    }
}
