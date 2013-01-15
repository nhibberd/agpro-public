package agpro.data.user;

import agpro.data.auth.HasUser;
import agpro.data.auth.ResultUser;

public class NewClient implements HasUser<NewClient> {
    public String user;
    public Integer growerID;
    public Boolean admin;
    public Integer repID;

    public NewClient(String user, Integer growerID, Boolean admin, Integer repID) {
        this.user = user;
        this.growerID = growerID;
        this.admin = admin;
        this.repID = repID;
    }

    public ResultUser<Integer> get() {
        if (repID==0)
            return ResultUser.error();
        else
            return ResultUser.ok(repID);
    }
}