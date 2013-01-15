package agpro.service.user;

import agpro.data.core.Function;
import agpro.data.core.Status;
import agpro.data.user.NewClient;
import agpro.db.EdgePreparedStatement;
import agpro.db.EdgeResultSet;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Date;

import static agpro.tool.Generators.generatorToken;
import static agpro.tool.Init.statement;
import static agpro.tool.Validations.checkrownu;

public class Client {


    public Status insertRegister(final NewClient z, Connection connection){
        final String token = generatorToken();
        final String sqlselect = "SELECT * FROM \"register\" WHERE \"growerid\" = ?";
        Boolean check = statement.withStatement(connection, sqlselect, new Function<PreparedStatement, Boolean>() {
            public Boolean apply(PreparedStatement preparedStatement) {
                EdgePreparedStatement edge = new EdgePreparedStatement(preparedStatement);
                edge.setInt(1,z.growerID);
                EdgeResultSet r = new EdgeResultSet(edge);
                return r.next();
            }
        });
        if(check)
            return Status.BAD_REQUEST;
        String sqlInsert = "INSERT INTO \"register\"( email, growerid, uuid, sent, admin, repid, timestamp ) VALUES (?, ?, ?, ?, ?, ?, ?)";
        return statement.withStatement(connection,sqlInsert,(new Function<PreparedStatement, Status>() {
            public Status apply(PreparedStatement statement) {
                EdgePreparedStatement q = new EdgePreparedStatement(statement);
                q.setString(1,z.user);
                q.setInt(2, z.growerID);
                q.setString(3, token);
                q.setBoolean(4, false);
                q.setBoolean(5, z.admin);
                q.setInt(6,z.repID);
                q.setLong(7,new Date().getTime());
                return checkrownu(q.executeUpdate());
            }
        }));
    }

    public Status insertAccount(final NewClient z, Connection connection){
        final String sqlselect = "SELECT * FROM \"account\" WHERE \"growerid\" = ?";
        Boolean check = statement.withStatement(connection, sqlselect, new Function<PreparedStatement, Boolean>() {
            public Boolean apply(PreparedStatement preparedStatement) {
                EdgePreparedStatement edge = new EdgePreparedStatement(preparedStatement);
                edge.setInt(1,z.growerID);
                EdgeResultSet r = new EdgeResultSet(edge);
                return r.next();
            }
        });
        if(check)
            return Status.OK;
        String sqlInsertAcccount = "INSERT INTO \"account\" ( growerid, email ) VALUES (?, ?)";
        return statement.withStatement(connection, sqlInsertAcccount, new Function<PreparedStatement, Status>() {
            public Status apply(PreparedStatement preparedStatement) {
                EdgePreparedStatement q = new EdgePreparedStatement(preparedStatement);
                q.setInt(1,z.growerID);
                q.setBoolean(2,true);
                return checkrownu(q.executeUpdate());
            }
        });
    }
}
