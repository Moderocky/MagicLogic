package mx.kenzie.magic.magic;

import mx.kenzie.overlord.Overlord;

public interface UnsafeModifier {

    UnsafeModifier GENERIC = new DefaultUnsafeModifier();

    default <T> T deepCopy(T obj) {
        return Overlord.deepClone(obj);
    }

    default <T> T shallowCopy(T obj) {
        return Overlord.shallowClone(obj);
    }

    default <T> T transform(Object obj, Class<T> cls) {
        return Overlord.transform(obj, cls);
    }

    default long getMemoryAddress(Object object) {
        return Overlord.getAddress(object);
    }

    final class DefaultUnsafeModifier implements UnsafeModifier {

    }

}
