package agpro.data.user;

public class RegMail {
    public String email;
    public Integer reggrowerid;
    public String regid;
    public Boolean sent;

    public RegMail(String email, Integer reggrowerid, String regid, Boolean sent) {
        this.email = email;
        this.reggrowerid = reggrowerid;
        this.regid = regid;
        this.sent = sent;
    }
}
