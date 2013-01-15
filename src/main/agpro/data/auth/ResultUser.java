package agpro.data.auth;

import agpro.data.error.ServerException;

public class ResultUser<T>{
    private final Boolean status;
    private final T value;

    public ResultUser(Boolean status, T value) {
        this.status = status;
        this.value = value;
    }

    public static <T> ResultUser<T> ok(T t) { return new ResultUser<T>(true, t); }
    public static <T> ResultUser<T> error() { return new ResultUser<T>(false, null); }

    public Boolean status() {
        return status;
    }

    public T value() {
        if (status())
            return value;
        throw new ServerException();
    }

}