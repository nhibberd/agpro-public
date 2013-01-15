package agpro.data.auth;

import agpro.data.grower.Grower;

public class LoginNugget {
    public String user;
    public String password;
    public Grower data;
    public long date;

    public LoginNugget(String user, String password, Grower data, long date) {
        this.user = user;
        this.password = password;
        this.data = data;
        this.date = date;
    }

}
