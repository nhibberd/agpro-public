package agpro.data.user;

import agpro.data.auth.HasUser;
import agpro.data.auth.ResultUser;

public class Register implements HasUser<Register> {
    public String user;
    public String password;
    public String password2;
    public Integer growerID;
    public Integer repID;
    public Boolean admin;

    public Register(String user, String password, String password2, Integer growerID, Integer repID, Boolean admin) {
        this.user = user;
        this.password = password;
        this.password2 = password2;
        this.growerID = growerID;
        this.repID = repID;
        this.admin = admin;
    }

    public ResultUser<Integer> get() {
        return ResultUser.error();
    }
}