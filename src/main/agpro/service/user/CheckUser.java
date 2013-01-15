package agpro.service.user;

import agpro.data.auth.Check;
import agpro.data.basic.Stringz;
import agpro.data.core.Function;
import agpro.data.core.Result;
import agpro.data.core.ResultFunction2;
import agpro.db.EdgePreparedStatement;
import agpro.db.EdgeResultSet;

import java.sql.Connection;
import java.sql.PreparedStatement;

import static agpro.tool.Init.statement;

public class CheckUser implements ResultFunction2<Connection,Stringz,Check> {
    public Result<Check> apply(final Connection connection, Stringz data) {
        final String uuid = data.string;
        String sqlSelect = "SELECT * FROM \"register\" WHERE \"uuid\"=?";
        return statement.withStatement(connection,sqlSelect, new Function<PreparedStatement, Result<Check>>() {
            public Result<Check> apply(PreparedStatement preparedStatement) {
                EdgePreparedStatement q = new EdgePreparedStatement(preparedStatement);
                q.setString(1,uuid);
                EdgeResultSet resultSet = new EdgeResultSet(q);
                if (resultSet.next()) {
                    return Result.ok(new Check(resultSet.getBoolean(5),resultSet.getInt(6)));
                }

                return Result.notfound();
            }
        });
    }
}

