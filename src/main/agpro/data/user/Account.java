package agpro.data.user;

import agpro.data.auth.HasUser;
import agpro.data.auth.ResultUser;

public class Account implements HasUser<Account> {
    public String usernew;
    public String newpassword;
    public String confirmnewpassword;
    public String currentpassword;
    public Boolean email;
    public Integer growerid;

    public Account(String usernew, String newpassword, String confirmnewpassword, String currentpassword, Boolean email, Integer growerid) {
        this.email = email;
        this.usernew = usernew;
        this.newpassword = newpassword;
        this.confirmnewpassword = confirmnewpassword;
        this.currentpassword = currentpassword;
        this.growerid = growerid;
    }

    @Override
    public String toString() {
        return "Account{" +
                "usernew='" + usernew + '\'' +
                ", newpassword='" + newpassword + '\'' +
                ", confirmnewpassword='" + confirmnewpassword + '\'' +
                ", currentpassword='" + currentpassword + '\'' +
                ", email=" + email +
                ", growerid=" + growerid +
                '}';
    }

    public ResultUser<Integer> get() {
        return ResultUser.ok(growerid);
    }
}
