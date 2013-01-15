package agpro.data.user;

import agpro.data.auth.HasUser;
import agpro.data.auth.ResultUser;

public class ResetPassword implements HasUser<ResetPassword> {
    public String token;
    public String password;
    public String password2;

    public ResetPassword(String password, String password2, String token) {
        this.password2 = password2;
        this.password = password;
        this.token = token;
    }

    public ResultUser<Integer> get() {
        return ResultUser.error();
    }
}