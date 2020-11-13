package mx.kenzie.magic.collection;

import mx.kenzie.magic.magic.Magic;

public interface MagicArrayWrapper extends Magic {

    boolean hasSpace();

    int entryCount();

    void trim();

    int size();

    boolean isEmpty();

    Object[] toArray();

    void clear();

    int count(Object o);

    int countEmpty();

    boolean hasEntryAfter(int index);

    int deadSpaceAtEnd();

    int findLastFilled();

    int findLastEmpty();

    int findFirstEmpty();

    int indexOf(Object o);

    int lastIndexOf(Object o);

    void resize(int size);

    void squash();
}
