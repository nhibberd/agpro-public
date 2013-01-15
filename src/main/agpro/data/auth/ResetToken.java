package agpro.data.auth;

public class ResetToken {
    public Integer number;
    public String token;
    public Boolean sent;

    public ResetToken(Integer number, String token, Boolean sent) {
        this.number = number;
        this.token = token;
        this.sent = sent;
    }
}