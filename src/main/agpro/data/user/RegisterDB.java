package agpro.data.user;

public class RegisterDB {
    public String email;
    public Integer growerID;
    public String uuid;
    public boolean sent;
    public boolean admin;
    public Integer repID;
    public Long timestamp;

    public RegisterDB(String email, Integer growerID, String uuid, boolean sent, boolean admin, Integer repID, Long timestamp) {
        this.email = email;
        this.growerID = growerID;
        this.uuid = uuid;
        this.sent = sent;
        this.admin = admin;
        this.repID = repID;
        this.timestamp = timestamp;
    }
}
