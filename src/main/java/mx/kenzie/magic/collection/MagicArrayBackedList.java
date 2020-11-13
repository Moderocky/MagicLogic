package mx.kenzie.magic.collection;

import mx.kenzie.magic.magic.UnsafeModifier;
import mx.kenzie.magic.note.Unsafe;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

@SuppressWarnings("unchecked")
public class MagicArrayBackedList<T> implements MagicGenericArrayWrapper<T>, List<T>, MagicArrayList<T> {

    public static final int DEFAULT_SIZE = 16;

    protected UnsafeModifier modifier;
    protected int pointer = -1;
    protected int empty = -1;
    protected T[] array;

    public MagicArrayBackedList(final int size) {
        this();
        this.resize(size);
    }

    public MagicArrayBackedList(final T... elements) {
        if (elements.length < 1) this.array = Arrays.copyOf(elements, DEFAULT_SIZE);
        else this.array = Arrays.copyOf(elements, elements.length);
    }

    public MagicArrayBackedList(final Class<T> cls) {
        array = (T[]) Array.newInstance(cls, DEFAULT_SIZE);
    }

    public MagicArrayBackedList(final Class<T> cls, int size) {
        array = (T[]) Array.newInstance(cls, size);
    }

    @SuppressWarnings({"CopyConstructorMissesField"})
    public MagicArrayBackedList(final MagicArrayBackedList<T> list) {
        this(list.array);
    }

    public static MagicArrayBackedList<Object> create() {
        return new MagicArrayBackedList<>();
    }

    public static MagicArrayBackedList<Object> create(final int size) {
        return new MagicArrayBackedList<>(size);
    }

    public static <T> MagicArrayBackedList<T> create(final Collection<T> iterable, final T[] array) {
        MagicArrayBackedList<T> collection = new MagicArrayBackedList<>(array);
        collection.addAll(iterable);
        return collection;
    }

    public boolean canAccept(final int amount) {
        return this.countEmpty() >= amount;
    }

    public void prepareFor(final int amount) {
        array = Arrays.copyOf(array, size() + amount);
    }

    @Override
    public boolean add(final T t) {
        final int index = pointer != -1 ? pointer++ : this.findFirstEmpty(true);
        if (index == -1) return false; // Should never happen.
        if (array[index] != null) {
            this.invalidate();
            array[this.findFirstEmpty(true)] = t;
        } else {
            this.array[index] = t;
        }
        empty--;
        return true;
    }

    protected int findFirstEmpty(final boolean shouldGrow) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == null) return i;
        }
        if (!shouldGrow) return -1;
        final int next = array.length;
        this.grow();
        return next;
    }

    protected void invalidate() {
        pointer = -1;
        empty = -1;
    }

    public void grow() {
        this.prepareFor(DEFAULT_SIZE);
    }

    @Override
    public Class<T> getComponentType() {
        return (Class<T>) array.getClass().getComponentType();
    }

    public static <T> MagicArrayBackedList<T> create(final Collection<T> iterable, final Class<T> componentType) {
        MagicArrayBackedList<T> collection = new MagicArrayBackedList<>(componentType, iterable.size());
        collection.addAll(iterable);
        return collection;
    }

    @Override
    public boolean hasSpace() {
        return indexOf(null) != -1;
    }

    @Override
    public int entryCount() {
        return array.length - countEmpty();
    }

    @Override
    public void trim() {
        this.invalidate();
        array = Arrays.copyOf(array, this.findLastFilled() + 1);
    }    @Override
    @SuppressWarnings("all")
    public boolean contains(final Object o) {
        return this.indexOf(o) != -1;
    }

    @Override
    public int size() {
        return array.length;
    }    @NotNull
    @Override
    public ArrayIterator iterator() {
        return new ArrayIterator();
    }

    @Override
    public boolean isEmpty() {
        return this.findFirstEmpty() == 0;
    }

    @NotNull
    @Override
    public T[] toArray() {
        return array.clone();
    }    @NotNull
    @Override
    public <T1> T1[] toArray(final @NotNull T1[] a) {
        if (a.length < 1) {
            final T1[] things = Arrays.copyOf(a, array.length);
            System.arraycopy(array, 0, things, 0, Math.min(things.length, array.length));
            return things;
        } else {
            System.arraycopy(array, 0, a, 0, Math.min(a.length, array.length));
            return a;
        }
    }

    @Override
    public void clear() {
        this.invalidate();
        this.array = (T[]) Array.newInstance(array.getClass().getComponentType(), array.length);
    }

    @Override
    public int count(final Object o) {
        int i = 0;
        for (T t : array) {
            if (Objects.equals(t, o)) i++;
        }
        return i;
    }    @Override
    public boolean remove(final Object o) {
        final int index = this.indexOf(o);
        if (index == -1) return false;
        this.array[index] = null;
        this.invalidate();
        return true;
    }

    @Override
    public int countEmpty() {
        if (empty > -1) return empty;
        int i = 0;
        for (int x = array.length - 1; x >= 0; x--) {
            if (array[x] == null) i++;
            pointer = x;
        }
        empty = i;
        return i;
    }    @Override
    @SuppressWarnings("all")
    public boolean containsAll(final @NotNull Collection<?> c) {
        final Object[] coll = c.toArray();
        for (Object o : coll) {
            if (this.indexOf(o) == -1) return false;
        }
        return true;
    }

    @Override
    public boolean hasEntryAfter(final int index) {
        for (int i = index; i < array.length - index; i++) {
            if (array[index] != null) return true;
        }
        return false;
    }    @Override
    public boolean addAll(final @NotNull Collection<? extends T> c) {
        int expected = c.size();
        if (expected < 1) return false;
        if (!this.canAccept(expected)) this.prepareFor(expected - this.countEmpty());
        boolean changed = false;
        for (T t : c) {
            if (this.add(t)) changed = true;
        }
        return changed;
    }

    @Override
    public int deadSpaceAtEnd() {
        int count = 0;
        for (int i = array.length - 1; i >= 0; i--) {
            if (array[i] != null) break;
            count++;
        }
        return count;
    }

    @Override
    public int findLastFilled() {
        for (int i = array.length - 1; i >= 0; i--) {
            if (array[i] != null) return i;
        }
        return -1;
    }    @Override
    public boolean removeAll(final @NotNull Collection<?> c) {
        boolean changed = false;
        for (Object t : c) {
            if (this.remove(t)) changed = true;
        }
        return changed;
    }

    @Override
    public int findLastEmpty() {
        for (int i = array.length - 1; i >= 0; i--) {
            if (array[i] == null) return i;
        }
        return -1;
    }    @Override
    public boolean retainAll(final @NotNull Collection<?> c) {
        boolean changed = false;
        final MagicArrayBackedList<Object> list = new MagicArrayBackedList<>(c.toArray());
        this.invalidate();
        for (int i = 0; i < array.length; i++) {
            if (!list.contains(array[i])) {
                array[i] = null;
                changed = true;
            }
        }
        return changed;
    }

    @Override
    public int findFirstEmpty() {
        return findFirstEmpty(false);
    }

    @Override
    public int indexOf(final Object o) {
        for (int i = 0; i < array.length; i++) {
            if (Objects.equals(array[i], o)) return i;
        }
        return -1;
    }

    @Override
    public int lastIndexOf(final Object o) {
        for (int i = array.length - 1; i >= 0; i--) {
            if (Objects.equals(array[i], o)) return i;
        }
        return -1;
    }

    @Override
    public void resize(final int size) {
        this.array = Arrays.copyOf(this.array, size);
    }

    @Override
    public void squash() {
        this.invalidate();
        final T[] things = Arrays.copyOf(array, this.entryCount());
        int index = 0;
        for (T t : array) {
            if (t == null) continue;
            things[index] = t;
            index++;
        }
        this.array = things;
    }

    public void chop(final int sizeToKeep) {
        this.resize(sizeToKeep);
    }

    @Override
    public boolean addAll(int index, final @NotNull Collection<? extends T> c) {
        int expected = c.size();
        if (expected < 1) return false;
        if (!this.canAccept(expected)) this.prepareFor(expected - deadSpaceAtEnd());
        try {
            final int last = this.findLastFilled();
            if (array[array.length - 1] != null)
                this.grow();
            if (last >= index) System.arraycopy(array, index, array, index + expected, (last - index) + expected);
            for (Object o : c.toArray()) {
                this.array[index++] = (T) o;
            }
        } finally {
            this.invalidate();
        }
        return true;
    }

    @Override
    public void sort(Comparator<? super T> c) {
        Arrays.sort(array, c);
        this.invalidate();
    }

    @Override
    public T get(final int index) {
        return array[index];
    }

    @Override
    public T set(final int index, final T element) {
        this.invalidate();
        final T thing = array[index];
        this.array[index] = element;
        return thing;
    }

    @Override
    public void add(final int index, final T element) {
        try {
            final int last = this.findLastFilled();
            if (array[array.length - 1] != null)
                this.grow();
            if (last >= index) System.arraycopy(array, index, array, index + 1, (last - index) + 1);
            this.array[index] = element;
        } finally {
            this.invalidate();
        }
    }

    @Override
    public T remove(final int index) {
        this.invalidate();
        final T thing = array[index];
        this.array[index] = null;
        return thing;
    }

    @NotNull
    @Override
    public ArrayIterator listIterator() {
        return new ArrayIterator();
    }

    @NotNull
    @Override
    public ArrayIterator listIterator(final int index) {
        return new ArrayIterator(index);
    }

    @NotNull
    @Override
    public MagicArrayBackedList<T> subList(final int fromIndex, final int toIndex) {
        assert fromIndex >= 0;
        assert toIndex < array.length;
        final MagicArrayBackedList<T> list = new MagicArrayBackedList<>((Class<T>) array.getClass().getComponentType(), toIndex - fromIndex);
        System.arraycopy(array, fromIndex, list.array, 0, list.array.length);
        return list;
    }

    @Override
    public MagicArrayBackedList<T> sort() {
        Arrays.sort(array, null);
        this.invalidate();
        return this;
    }

    @Override
    public MagicArrayBackedList<T> until(final int end) {
        return subList(0, end - 1);
    }

    @Override
    public <U, R> MagicArrayBackedList<R> collect(final BiFunction<T, U, R> function, final U argument) {
        final MagicArrayBackedList<R> list = new MagicArrayBackedList<>();
        list.resize(array.length);
        for (T t : array) {
            list.add(function.apply(t, argument));
        }
        return list;
    }

    @Override
    public MagicArrayBackedList<T> filter(final Predicate<T> function) {
        final MagicArrayBackedList<T> list = new MagicArrayBackedList<>();
        list.resize(array.length);
        for (T t : array) {
            if (function.test(t)) list.add(t);
        }
        list.resize(list.findLastFilled() + 1);
        return list;
    }

    @Override
    public <Q> MagicArrayBackedList<T> filterFind(final Q sample, final BiFunction<T, Q, Boolean> function) {
        final MagicArrayBackedList<T> list = new MagicArrayBackedList<>();
        list.resize(array.length);
        for (T t : array) {
            if (function.apply(t, sample)) list.add(t);
        }
        list.resize(list.findLastFilled() + 1);
        return list;
    }

    @Override
    public <Q> T filterFindFirst(final Q sample, final BiFunction<T, Q, Boolean> function) {
        for (T t : array) {
            if (function.apply(t, sample)) return t;
        }
        return null;
    }

    @Override
    public <Q> T filterFindLast(final Q sample, final BiFunction<T, Q, Boolean> function) {
        for (int i = array.length - 1; i >= 0; i--) {
            if (function.apply(array[i], sample)) return array[i];
        }
        return null;
    }

    @Override
    public <Q> MagicArrayBackedList<Q> castConvert(final Class<Q> cls) {
        return this.collect(t -> (Q) t);
    }

    @Override
    public <Q> MagicArrayBackedList<Q> castConvert() {
        return this.collect(t -> (Q) t);
    }

    @Override
    public <R> MagicArrayBackedList<R> collect(final Function<T, R> function) {
        final MagicArrayBackedList<R> list = new MagicArrayBackedList<>();
        for (T t : array) {
            list.add(function.apply(t));
        }
        return list;
    }

    @Override
    public void shuffle() {
        Collections.shuffle(this);
    }

    @Override
    public MagicArrayBackedList<T> from(final int start, final int end) {
        return subList(start, end - 1);
    }

    @Override
    public MagicArrayBackedList<T> from(final int start) {
        return subList(start, array.length - 1);
    }

    @Override
    @Unsafe("Allows access to dangerous magical powers.")
    public MagicArrayBackedList<T> allowUnsafe() {
        modifier = UnsafeModifier.GENERIC;
        return this;
    }

    @Override
    @Unsafe
    public MagicArrayBackedList<T> deepCopy() {
        assert modifier != null;
        return this.collect(modifier::deepCopy);
    }

    @Override
    @Unsafe
    public MagicArrayBackedList<T> shallowCopy() {
        assert modifier != null;
        return this.collect(modifier::shallowCopy);
    }

    @Override
    @Unsafe("Object transformations are *very* dangerous.")
    @Deprecated
    public <Q> MagicArrayBackedList<Q> transform(final Class<Q> target) {
        assert modifier != null;
        return this
                .collect(modifier::shallowCopy)
                .collect(modifier::transform, target);
    }

    @Override
    @Unsafe("What are you planning...")
    @Deprecated
    public long getMemoryAddress() {
        assert modifier != null;
        return modifier.getMemoryAddress(this);
    }

    @Override
    public String toString() {
        return Arrays.toString(array);
    }

    protected class ArrayIterator implements ListIterator<T> {

        protected transient int pointer = -1;

        public ArrayIterator() {
        }

        public ArrayIterator(final int index) {
            pointer = index;
        }

        @Override
        public boolean hasNext() {
            return pointer < array.length - 1;
        }

        @Override
        public T next() {
            return array[++pointer];
        }

        @Override
        public boolean hasPrevious() {
            return pointer > 0;
        }

        @Override
        public T previous() {
            return array[--pointer];
        }

        @Override
        public int nextIndex() {
            return pointer + 1;
        }

        @Override
        public int previousIndex() {
            return pointer - 1;
        }

        @Override
        public void remove() {
            array[pointer] = null;
            invalidate();
        }

        @Override
        public void set(final T t) {
            array[pointer] = t;
            invalidate();
        }

        @Override
        public void add(final T t) {
            MagicArrayBackedList.this.add(t);
        }
    }

















}
