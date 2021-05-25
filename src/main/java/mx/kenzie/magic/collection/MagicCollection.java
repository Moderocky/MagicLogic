package mx.kenzie.magic.collection;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public interface MagicCollection<E> extends Collection<E> {

    default MagicCollection<E> addAnd(E thing) {
        this.add(thing);
        return this;
    }

    @Override
    boolean add(E e);

    default MagicCollection<E> removeAnd(E thing) {
        this.remove(thing);
        return this;
    }

    default MagicCollection<E> removeAnd(int index) {
        this.remove(index);
        return this;
    }

    default MagicCollection<E> stack(MagicCollection<E>... collections) {
        for (MagicCollection<E> ts : collections) {
            this.addAll(ts);
        }
        return this;
    }

    default MagicCollection<E> stack(E[]... arrays) {
        for (E[] ts : arrays) {
            this.addAll(ts);
        }
        return this;
    }

    default void addAll(E... ts) {
        addAll(Arrays.asList(ts));
    }

    default boolean removeUnless(Predicate<? super E> filter) {
        return this.removeIf(filter.negate());
    }

    default int countMatches(Function<E, Boolean> function) {
        int i = 0;
        for (E thing : this) {
            if (function.apply(thing)) i++;
        }
        return i;
    }

    default boolean allMatch(Function<E, Boolean> function) {
        for (E thing : this) {
            if (!function.apply(thing)) return false;
        }
        return true;
    }

    default boolean oneMatches(Function<E, Boolean> function) {
        for (E E : this) {
            if (function.apply(E)) return true;
        }
        return false;
    }

    default boolean noMatches(Function<E, Boolean> function) {
        for (E E : this) {
            if (!function.apply(E)) return false;
        }
        return true;
    }

    default MagicCollection<E> forEachAnd(Consumer<? super E> action) {
        this.forEach(action);
        return this;
    }
    
    default <Q> MagicCollection<E> forAllAnd(Q input, BiConsumer<? super E, Q> action) {
        forAll(input, action);
        return this;
    }

    default <Q> MagicCollection<E> forAllAndR(Q input, BiConsumer<Q, E> action) {
        forAllR(input, action);
        return this;
    }
    
    default <Q> Q forAllAndReturn(Q input, BiConsumer<? super E, Q> action) {
        forAll(input, action);
        return input;
    }
    
    default <Q> Q forAllAndReturnR(Q input, BiConsumer<Q, E> action) {
        forAllR(input, action);
        return input;
    }
    
    default <Q> void forAll(Q input, BiConsumer<? super E, Q> action) {
        for (E E : this) {
            action.accept(E, input);
        }
    }
    
    default <Q> void forAllR(Q input, BiConsumer<Q, ? super E> action) {
        for (E E : this) {
            action.accept(input, E);
        }
    }

    default boolean removeNull() {
        final int size = this.size();
        removeIf(Objects::isNull);
        return this.size() != size;
    }

    int indexOf(Object o);

    Class<E> getComponentType();

}
