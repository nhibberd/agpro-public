package agpro.data.auth;

public class Token implements HasUser<Token> {
    public Integer number;
    public String token;

    public Token(Integer number, String token) {
        this.number = number;
        this.token = token;
    }


    public ResultUser<Integer> get() {
        return ResultUser.error();
    }
}