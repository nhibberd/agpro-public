package agpro.service.user;

import agpro.data.core.Action;
import agpro.data.core.Function2;
import agpro.data.core.Status;
import agpro.data.user.Account;
import agpro.db.EdgePreparedStatement;
import agpro.db.EdgeResultSet;
import agpro.service.auth.LoginServices;
import agpro.service.rep.Profile;

import java.sql.Connection;
import java.sql.PreparedStatement;

import static agpro.tool.Init.statement;

public class UpdateAccount implements Function2<Connection,Account,Status> {
    private LoginServices login = new LoginServices();
    private UserServices service = new UserServices();
    private Profile profile = new Profile();

    public Status apply(final Connection connection, final Account account) {
        if (!login.authenticate(connection,account.growerid,account.currentpassword))
            return Status.NOT_AUTH;
        final String[] user = {null};
        final Boolean[] admin = {null};

        String sql = "SELECT * FROM \"users\" WHERE growerid=?";
        statement.withStatement(connection,sql,(new Action<PreparedStatement>() {
            public void apply(PreparedStatement statement) {
                EdgePreparedStatement q = new EdgePreparedStatement(statement);
                q.setInt(1, account.growerid);
                EdgeResultSet z = new EdgeResultSet(q);
                if (z.next()) {
                    user[0] = z.getString(1);
                    admin[0] = z.getBoolean(5);
                }
            }
        }));

        Status r = null;
        if((!account.newpassword.equals("") && !account.confirmnewpassword.equals(""))
                && (account.newpassword.equals(account.confirmnewpassword))){
            r = service.updateUser(connection, user[0], account.newpassword, admin[0], account.growerid);
        }

        if((!account.usernew.equals(""))) {
            r = service.updateUser(connection, account.usernew, account.currentpassword, admin[0], account.growerid);
            profile.updateProfile(connection,account.usernew,account.growerid);
        }

        if((account.email != null))
            r = service.updateAccount(connection, account.growerid,account.email);

        return r;
    }
}