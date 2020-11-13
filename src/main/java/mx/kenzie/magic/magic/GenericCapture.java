package mx.kenzie.magic.magic;

import java.util.Collection;

public class GenericCapture<T> {

    protected final Class<T> type;

    @SafeVarargs
    @SuppressWarnings("all")
    public GenericCapture(T... capture) {
        type = (Class<T>) capture.getClass().getComponentType();
    }

    public GenericCapture(Collection<T> sample, T... capture) {
        type = (Class<T>) capture.getClass().getComponentType();
    }

    public Class<T> getType() {
        return type;
    }

}
