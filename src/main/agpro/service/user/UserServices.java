package agpro.service.user;

import agpro.data.core.Function;
import agpro.data.core.Status;
import agpro.data.user.User;
import agpro.db.EdgePreparedStatement;
import agpro.db.EdgeResultSet;
import agpro.service.auth.LoginServices;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;

import static agpro.tool.Base64.byteToBase64;
import static agpro.tool.Init.statement;
import static agpro.tool.Validations.checkrownu;

public class UserServices {
    private final LoginServices service = new LoginServices();
    private static final SecureRandom generator = random();

    public User getUser(Connection connection, final String user){
        final String sql = "SELECT * FROM \"users\" WHERE \"username\" = ?";
        return statement.withStatement(connection,sql, new Function<PreparedStatement, User>() {
            public User apply(PreparedStatement preparedStatement) {
                EdgePreparedStatement q = new EdgePreparedStatement(preparedStatement);
                q.setObject(1,user);
                return service.userResult(q);
            }
        });
    }

    public Boolean exists(Connection connection, final Integer id){
        final String sql = "SELECT * FROM \"users\" WHERE \"growerid\" = ?";
        return statement.withStatement(connection,sql, new Function<PreparedStatement, Boolean>() {
            public Boolean apply(PreparedStatement preparedStatement) {
                EdgePreparedStatement q = new EdgePreparedStatement(preparedStatement);
                q.setObject(1,id);
                EdgeResultSet resultSet = new EdgeResultSet(q);
                return resultSet.next();
            }
        });
    }

    public Boolean exists(Connection connection, final String user){
        final String sql = "SELECT * FROM \"users\" WHERE \"username\" = ?";
        return statement.withStatement(connection,sql, new Function<PreparedStatement, Boolean>() {
            public Boolean apply(PreparedStatement preparedStatement) {
                EdgePreparedStatement q = new EdgePreparedStatement(preparedStatement);
                q.setObject(1,user);
                EdgeResultSet resultSet = new EdgeResultSet(q);
                return resultSet.next();
            }
        });
    }

    public User getUser(Connection connection, final Integer id){
        final String sql = "SELECT * FROM \"users\" WHERE \"growerid\" = ?";
        return statement.withStatement(connection,sql, new Function<PreparedStatement, User>() {
            public User apply(PreparedStatement preparedStatement) {
                EdgePreparedStatement q = new EdgePreparedStatement(preparedStatement);
                q.setObject(1,id);
                return service.userResult(q);
            }
        });
    }

    public Status updateUser(Connection connection, final String user, final String password, final Boolean admin, final Integer growerID) {
        if(user!=null && password!=null && user.length()<=100) {
            if (!service.checkuser(connection, growerID))
                return Status.NOT_FOUND;

            //Encrypt data
            byte[] salt = new byte[10];
            generator.nextBytes(salt);
            final String sDigest = encrypt(password,salt);
            final String sSalt = byteToBase64(salt);

            //Insert data into db
            String sqlInsert = "UPDATE \"users\" SET username=?, password=?, salt=?, admin=? WHERE growerid=?";
            return statement.withStatement(connection,sqlInsert,(new Function<PreparedStatement, Status>() {
                public Status apply(PreparedStatement statement) {
                    EdgePreparedStatement q = new EdgePreparedStatement(statement);
                    q.setString(1,user);
                    q.setString(2, sDigest);
                    q.setString(3,sSalt);
                    q.setBoolean(4,admin);
                    q.setInt(5,growerID);
                    return checkrownu(q.executeUpdate());

                }
            }));
        } else
            return Status.BAD_REQUEST;
    }

    private String encrypt(String password, byte[] salt){
        byte[] passDigest = service.getHash(password, salt);
        return byteToBase64(passDigest);
    }

    private static SecureRandom random() {
        try {
            return SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }


    public Status updateAccount(Connection connection, final Integer id, final Boolean email) {
        String sqlInsert = "UPDATE \"account\" SET email=? WHERE growerid=?";
        return statement.withStatement(connection,sqlInsert,(new Function<PreparedStatement, Status>() {
            public Status apply(PreparedStatement statement) {
                EdgePreparedStatement q = new EdgePreparedStatement(statement);
                q.setBoolean(1,email);
                q.setInt(2, id);
                return checkrownu(q.executeUpdate());
            }
        }));
    }

}
