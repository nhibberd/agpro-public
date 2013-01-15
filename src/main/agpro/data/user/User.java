package agpro.data.user;


public class User {
    public Boolean admin;
    public String user;
    public byte[] password;
    public Integer number;
    public byte[] salt;


    public User(String user, byte[] password, Integer number, byte[] salt, Boolean admin) {
        this.salt = salt;
        this.number = number;
        this.password = password;
        this.user = user;
        this.admin = admin;
    }

    @Override
    public String toString() {
        return "User{" +
                "admin=" + admin +
                ", user='" + user + '\'' +
                ", password=" + password +
                ", number=" + number +
                ", salt=" + salt +
                '}';
    }
}
