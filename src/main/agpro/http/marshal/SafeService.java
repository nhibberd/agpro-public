package agpro.http.marshal;

import agpro.data.core.Function;
import agpro.data.core.Return;

public class SafeService<A> implements Function<A, Return> {
    private final Function<A, Return> delegate;

    public SafeService(Function<A, Return> delegate) {
        this.delegate = delegate;
    }

    public Return apply(A a) {
        try {
            return delegate.apply(a);
        } catch (Exception e) {
            return new Return(false, "Unexpexted error [" + e.getMessage() + "]");
        }
    }
}
