package agpro.data.auth;

public class Login implements HasUser<Login> {
    public String user;
    public String password;

    public Login(String password, String user) {
        this.password = password;
        this.user = user;
    }

    public ResultUser<Integer> get() {
        return ResultUser.error();
    }
}
