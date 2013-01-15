package agpro.data.core;

public interface Action<A> {
    void apply(A a);
}
