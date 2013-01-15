package agpro.service.user;

import agpro.data.core.Function;
import agpro.data.core.Function2;
import agpro.data.core.Status;
import agpro.data.user.NewClient;
import agpro.data.user.Register;
import agpro.db.EdgePreparedStatement;
import agpro.service.auth.LoginServices;
import agpro.service.grower.Personal;
import agpro.service.grower.Questionnaire;
import agpro.service.rep.Profile;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;

import static agpro.tool.Base64.byteToBase64;
import static agpro.tool.Init.statement;
import static agpro.tool.Validations.checkrownu;


public class CreateUser implements Function2<Connection,Register,Status> {
    private final LoginServices service = new LoginServices();
    private static final SecureRandom generator = random();
    public Status apply(Connection connection, final Register data) {
        if(data.user!=null && data.password!=null && data.password2!=null && data.user.length()<=100 && data.password.equals(data.password2)) {
            if (service.checkuser(connection, data.user))
                return Status.BAD_REQUEST;//throw new ServerException("User already exists " + data.user);
            if (service.checkuser(connection, data.growerID))
                return Status.BAD_REQUEST; //throw new ServerException("ID already exists " + data.growerID);

            //Encrypt data
            byte[] salt = new byte[10];
            generator.nextBytes(salt);
            final String sDigest = encrypt(data.password, salt);
            final String sSalt = byteToBase64(salt);

            //Insert data into db
            String sqlInsert = "INSERT INTO \"users\"( username, password, growerid, salt, admin ) VALUES (?, ?, ?, ?, ?)";
            Status insertUser = statement.withStatement(connection,sqlInsert,(new Function<PreparedStatement, Status>() {
                public Status apply(PreparedStatement statement) {
                    EdgePreparedStatement q = new EdgePreparedStatement(statement);
                    q.setString(1,data.user);
                    q.setString(2,sDigest);
                    q.setInt(3, data.growerID);
                    q.setString(4, sSalt);
                    q.setBoolean(5, data.admin);
                    //q.set(user,sDigest,growerID,sSalt);

                    return checkrownu(q.executeUpdate());
                }
            }));


            if (insertUser == Status.OK) {
                Client clientService = new Client();
                Profile profile = new Profile();
                Personal details = new Personal();
                Questionnaire question = new Questionnaire();
                NewClient client = new NewClient(data.user,data.growerID,data.admin,data.repID);

                clientService.insertAccount(client, connection);                //admin + client
                profile.createEmptyProfile(connection, client);                 //admin + client
                if (!data.admin){
                    details.createEmpty(connection, client.growerID,client.admin);  //client
                    question.createEmpty(connection, client.growerID);              //client
                    profile.createRepClient(connection, client);                    //client
                }
                return Status.OK;
            }
        }
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
}
