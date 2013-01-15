package agpro.service.user;

import agpro.data.core.Function2;
import agpro.data.core.Status;
import agpro.data.user.NewClient;

import java.sql.Connection;

public class AddUser implements Function2<Connection,NewClient,Status> {
    private final Client service = new Client();
    private final UserServices user = new UserServices();

    public Status apply(Connection connection, NewClient client) {
        System.out.println("hERE");

        if (user.exists(connection,client.growerID))
            return Status.BAD_REQUEST;
        System.out.println("hERE2");
        if (user.exists(connection,client.user))
            return Status.BAD_REQUEST;
        System.out.println("hERE3");
        return service.insertRegister(client, connection);
    }
}
