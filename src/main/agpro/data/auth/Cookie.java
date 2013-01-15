package agpro.data.auth;

import agpro.data.grower.Grower;

public class Cookie {
    public Grower grower;
    public String uid;

    public Cookie(Grower grower, String uid) {
        this.grower = grower;
        this.uid = uid;
    }
}
