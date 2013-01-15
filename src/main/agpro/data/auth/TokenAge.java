package agpro.data.auth;

public class TokenAge {
    public Token token;
    public long age;

    public TokenAge(Token token, long age) {
        this.age = age;
        this.token = token;
    }
}
