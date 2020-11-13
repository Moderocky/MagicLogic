package mx.kenzie.magic.collection;

import mx.kenzie.magic.note.Unsafe;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.*;

public interface MagicArrayList<T> extends Collection<T>, List<T>, RandomAccess, Cloneable, Serializable, MagicCollection<T> {

    default MagicArrayList<T> addAnd(T t) {
        this.add(t);
        return this;
    }

    default MagicArrayList<T> removeAnd(T t) {
        this.remove(t);
        return this;
    }

    default MagicArrayList<T> removeAnd(int index) {
        this.remove(index);
        return this;
    }

    default MagicArrayList<T> stack(T[]... arrays) {
        for (T[] array : arrays) {
            addAll(array);
        }
        return this;
    }

    default boolean removeUnless(Predicate<? super T> filter) {
        return this.removeIf(filter.negate());
    }

    default int countMatches(Function<T, Boolean> function) {
        int i = 0;
        for (T t : this) {
            if (function.apply(t)) i++;
        }
        return i;
    }

    default boolean allMatch(Function<T, Boolean> function) {
        for (T t : this) {
            if (!function.apply(t)) return false;
        }
        return true;
    }

    default boolean oneMatches(Function<T, Boolean> function) {
        for (T t : this) {
            if (function.apply(t)) return true;
        }
        return false;
    }

    default boolean noMatches(Function<T, Boolean> function) {
        for (T t : this) {
            if (!function.apply(t)) return false;
        }
        return true;
    }

    default MagicArrayList<T> forEachAnd(Consumer<? super T> action) {
        this.forEach(action);
        return this;
    }

    default <Q> void forAll(Q input, BiConsumer<? super T, Q> action) {
        for (T t : this) {
            action.accept(t, input);
        }
    }

    default <Q> MagicArrayList<T> forAllAnd(Q input, BiConsumer<? super T, Q> action) {
        forAll(input, action);
        return this;
    }

    @Override
    default void addAll(T... ts) {
        addAll(Arrays.asList(ts));
    }

    default MagicArrayList<T> shuffleAnd() {
        Collections.shuffle(this);
        return this;
    }

    default MagicArrayList<T> sort() {
        this.sort(null);
        return this;
    }

    default MagicArrayList<T> stack(Collection<T>... collections) {
        for (Collection<T> collection : collections) {
            addAll(collection);
        }
        return this;
    }

    MagicArrayList<T> until(int end);

    <U, R> MagicArrayList<R> collect(BiFunction<T, U, R> function, U argument);

    MagicArrayList<T> filter(Predicate<T> function);

    <Q> MagicArrayList<T> filterFind(Q sample, BiFunction<T, Q, Boolean> function);

    <Q> T filterFindFirst(Q sample, BiFunction<T, Q, Boolean> function);

    <Q> T filterFindLast(Q sample, BiFunction<T, Q, Boolean> function);

    <Q> MagicArrayList<Q> castConvert(Class<Q> cls);

    <Q> MagicArrayList<Q> castConvert();

    default T getRandom() {
        return getRandom(ThreadLocalRandom.current());
    }

    default T getRandom(@NotNull Random random) {
        return get(random.nextInt(size()));
    }

    default T removeRandom() {
        return removeRandom(ThreadLocalRandom.current());
    }

    default T removeRandom(@NotNull Random random) {
        return remove(random.nextInt(size()));
    }

    default MagicStringList asMagicStrings() {
        MagicStringList list = new MagicStringList();
        if (!this.isEmpty()) {
            list.addAll(this.collect(Objects::toString));
        }
        return list;
    }

    <R> MagicArrayList<R> collect(Function<T, R> function);

    default MagicStringList asMagicStrings(Function<T, String> converter) {
        MagicStringList list = new MagicStringList();
        if (!this.isEmpty()) {
            list.addAll(this.collect(converter));
        }
        return list;
    }

    default void addAll(int i, T... ts) {
        addAll(i, Arrays.asList(ts));
    }

    default <C extends Collection<Q>, Q> C addConvertedTo(C collection, Function<T, Q> converter) {
        for (T thing : this) {
            collection.add(converter.apply(thing));
        }
        return collection;
    }

    default <C extends Collection<T>> C convertTo(Supplier<C> supplier) {
        C collection = supplier.get();
        collection.addAll(this);
        return collection;
    }

    void shuffle();

    default MagicArrayList<T> getFirst(int amount) {
        return from(0, amount);
    }

    MagicArrayList<T> from(int start, int end);

    default MagicArrayList<T> getLast(int amount) {
        return from(size() - amount);
    }

    MagicArrayList<T> from(int start);

    default T getFirst() {
        if (size() < 1) return null;
        return get(0);
    }

    default T getLast() {
        if (size() < 1) return null;
        return get(this.size() - 1);
    }

    @Unsafe("Allows access to dangerous magical powers.")
    MagicArrayList<T> allowUnsafe();

    @Unsafe
    MagicArrayList<T> deepCopy();

    @Unsafe
    MagicArrayList<T> shallowCopy();

    @Unsafe("Object transformations are *very* dangerous.")
    @Deprecated
    <Q> MagicArrayList<Q> transform(Class<Q> target);

    @Unsafe("What are you planning...")
    @Deprecated
    long getMemoryAddress();

    default ArrayList<T> toArrayList() {
        return new ArrayList<>(this);
    }

    default Set<T> toSet() {
        return new HashSet<>(this);
    }
}
