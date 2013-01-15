package agpro.service.contact;

import agpro.data.contact.Contact;
import agpro.data.core.Function;
import agpro.data.core.Function2;
import agpro.data.core.Status;
import agpro.db.EdgePreparedStatement;
import agpro.service.admin.ContactList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

import static agpro.tool.Init.statement;
import static agpro.tool.Validations.checkrownu;

public class Contactus implements Function2<Connection,Contact,Status> {
    private final ContactList contacts = new ContactList();

    public Status apply(Connection connection, final Contact contact) {
        final String message = "You have recieved a 'Contact Us' message. The details of the message can be seen below.<br></br><br></br><b>Contact information</b><br></br><br></br><b>Name: </b>" + contact.name + "<br></br><b>Phone: </b>" + contact.phone + "<br></br><b>Email: </b>" + contact.email + "<br></br><b>Message: </b>" + contact.message + "<br></br><br></br><i>To unsubscribe from recieving emails uncheck the recieve email notifications box on your account details page</i>";

        List<String> list = contacts.get(connection);
        for (final String email : list) {
            String sqlInsert = "INSERT INTO \"mail\"( email, subject, content ) VALUES (?, ?, ?)";
            Status status = statement.withStatement(connection, sqlInsert, new Function<PreparedStatement, Status>() {
                public Status apply(PreparedStatement preparedStatement) {
                    EdgePreparedStatement z = new EdgePreparedStatement(preparedStatement);
                    z.setString(1, email);
                    z.setString(2, contact.subject);
                    z.setString(3, message);
                    return checkrownu(z.executeUpdate());
                }
            });
            if (status == Status.NOT_FOUND)
                return status;
        }
        return Status.OK;
    }
}
