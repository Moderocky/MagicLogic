package mx.kenzie.magic.collection;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import mx.kenzie.magic.magic.GenericCapture;
import mx.kenzie.magic.magic.JsonMapper;
import mx.kenzie.magic.magic.UnsafeModifier;
import mx.kenzie.magic.note.Unsafe;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;

/**
 * A specialised ArrayList wrapper with extra features for quick and easy use.
 *
 * @param <T> Collection type
 * @author Mackenzie
 */
public class MagicList<T> extends ArrayList<T> implements MagicArrayList<T> {

    protected UnsafeModifier modifier;

    public MagicList(int capacity) {
        super(capacity);
    }

    public MagicList(Collection<? extends T> collection) {
        super(collection);
    }

    public MagicList(Iterable<? extends T> iterable) {
        this();
        for (T t : iterable) {
            this.add(t);
        }
    }

    public MagicList() {
        super();
    }

    @SafeVarargs
    public MagicList(T... ts) {
        super(Arrays.asList(ts));
    }

    public static MagicStringList ofWords(String string) {
        return new MagicStringList(string.split("\\s+"));
    }

    public static MagicStringList from(Iterable<?> iterable) {
        if (iterable == null) return new MagicStringList();
        MagicStringList list = new MagicStringList();
        for (Object element : iterable) {
            if (element != null)
                list.add(element.toString());
        }
        return list;
    }

    public static <T, Q> MagicList<T> from(Iterable<Q> iterable, Function<Q, T> converter) {
        if (iterable == null || converter == null) return new MagicList<>();
        MagicList<T> list = new MagicList<>();
        for (Q element : iterable) {
            list.add(converter.apply(element));
        }
        return list;
    }

    @SafeVarargs
    public static <T> MagicList<T> from(Collection<T>... collections) {
        MagicList<T> list = new MagicList<>();
        for (Collection<T> collection : collections) {
            list.addAll(collection);
        }
        return list;
    }

    @SafeVarargs
    public static <T> MagicList<T> from(Iterable<T>... iterables) {
        MagicList<T> list = new MagicList<>();
        for (Iterable<T> collection : iterables) {
            for (T thing : collection) {
                list.add(thing);
            }
        }
        return list;
    }

    @Override
    public MagicList<T> addAnd(T t) {
        this.add(t);
        return this;
    }

    @Override
    public MagicList<T> removeAnd(T t) {
        this.remove(t);
        return this;
    }

    @Override
    public MagicList<T> removeAnd(int index) {
        this.remove(index);
        return this;
    }

    @Override
    public MagicList<T> shuffleAnd() {
        this.shuffle();
        return this;
    }

    @Override
    public MagicList<T> sort() {
        this.sort(null);
        return this;
    }

    @Override
    @SafeVarargs
    public final MagicList<T> stack(Collection<T>... collections) {
        for (Collection<T> ts : collections) {
            this.addAll(ts);
        }
        return this;
    }

    @Override
    @SafeVarargs
    public final MagicList<T> stack(T[]... arrays) {
        for (T[] ts : arrays) {
            this.addAll(ts);
        }
        return this;
    }

    @Override
    public MagicList<T> until(int until) {
        return from(0, until);
    }

    @Override
    public MagicList<T> from(int start) {
        return from(start, size());
    }

    @Override
    public MagicList<T> from(int start, int end) {
        if (start < 0) throw new IllegalArgumentException("Start index must be >= 0!");
        if (end > size()) throw new IllegalArgumentException("End index must not be greater than the list's size!");
        return new MagicList<>(subList(start, end - 1));
    }

    @Override
    public boolean removeUnless(Predicate<? super T> filter) {
        return this.removeIf(filter.negate());
    }

    @Override
    public <R> MagicList<R> collect(Function<T, R> function) {
        MagicList<R> list = new MagicList<>();
        for (T thing : this) {
            list.add(function.apply(thing));
        }
        return list;
    }

    @Override
    public <U, R> MagicList<R> collect(BiFunction<T, U, R> function, U argument) {
        MagicList<R> list = new MagicList<>();
        for (T thing : this) {
            list.add(function.apply(thing, argument));
        }
        return list;
    }

    @Override
    public MagicList<T> filter(Predicate<T> function) {
        return this.stream().filter(function).collect(Collectors.toCollection(MagicList::new));
    }

    @Override
    public <Q> MagicList<T> filterFind(Q sample, BiFunction<T, Q, Boolean> function) {
        return this.stream().filter(t -> function.apply(t, sample)).collect(Collectors.toCollection(MagicList::new));
    }

    //    public MagicMap<Integer, T> toIndexMap() {
//        return MagicMap.ofIndices(this);
//    }

    @Override
    public <Q> T filterFindFirst(Q sample, BiFunction<T, Q, Boolean> function) {
        return this.stream().filter(t -> function.apply(t, sample)).findFirst().orElse(null);
    }

    @Override
    public <Q> T filterFindLast(Q sample, BiFunction<T, Q, Boolean> function) {
        List<T> list = new ArrayList<>(this);
        Collections.reverse(list);
        return list.stream().filter(t -> function.apply(t, sample)).findFirst().orElse(null);
    }
    
    @Override
    public MagicList<T> forEachAnd(Consumer<? super T> action) {
        this.forEach(action);
        return this;
    }
    
    @Override
    public <Q> MagicList<T> forAllAnd(Q input, BiConsumer<? super T, Q> action) {
        forAll(input, action);
        return this;
    }
    
    @Override
    public <Q> MagicList<T> forAllAndR(Q input, BiConsumer<Q, T> action) {
        forAllR(input, action);
        return this;
    }
    
    @Override
    public <Q> MagicList<Q> castConvert(Class<Q> cls) {
        return castConvert();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <Q> MagicList<Q> castConvert() {
        MagicList<Q> list = new MagicList<>();
        for (T thing : this) {
            list.add((Q) thing);
        }
        return list;
    }

    @Override
    public void shuffle() {
        Collections.shuffle(this);
    }

    @Override
    @Unsafe("Allows access to dangerous magical powers.")
    public MagicList<T> allowUnsafe() {
        modifier = UnsafeModifier.GENERIC;
        return this;
    }

    @Override
    @Unsafe
    public MagicList<T> deepCopy() {
        assert modifier != null;
        return this.collect(modifier::deepCopy);
    }

    @Override
    @Unsafe
    public MagicList<T> shallowCopy() {
        assert modifier != null;
        return this.collect(modifier::shallowCopy);
    }

    @Override
    @Unsafe("Object transformations are *very* dangerous.")
    @Deprecated
    public <Q> MagicList<Q> transform(Class<Q> target) {
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

    public MagicList<T> reverseSort() {
        this.sort(Collections.reverseOrder());
        return this;
    }

    @SuppressWarnings("unchecked")
    public MagicList<T>[] split(int size) {
        Object[] array = this.toArray();
        List<MagicList<T>> lists = new ArrayList<>();
        int count = 0;
        MagicList<T> list = new MagicList<>();
        for (Object o : array) {
            count++;
            if (count > size) {
                lists.add(list);
                list = new MagicList<>();
                count = 0;
            }
            list.add((T) o);
        }
        return (MagicList<T>[]) lists.toArray(new MagicList[0]);
    }

    public <R> MagicList<R> collectIgnoreNull(Function<T, R> function) {
        MagicList<R> list = new MagicList<>();
        for (T thing : this) {
            if (thing == null) continue;
            R blob = function.apply(thing);
            if (blob != null) list.add(blob);
        }
        return list;
    }

    @SuppressWarnings("unchecked")
    public T[] toArray(Class<T> cls) {
        return super.toArray((T[]) Array.newInstance(cls, 0));
    }

    public boolean removeDuplicates() {
        final int s = this.size();
        Set<T> set = new HashSet<>(this);
        this.clear();
        this.addAll(set);
        return s != this.size();
    }

    @Override
    @SuppressWarnings("unchecked")
    public MagicList<T> clone() {
        try {
            return (MagicList<T>) super.clone();
        } catch (Throwable ex) { // CloneNotSupportedException or ClassCastException
            if (modifier != null)
                return modifier.shallowCopy(this);
            return new MagicList<>(this);
        }
    }

    @Override
    public void sort(Comparator<? super T> c) {
        super.sort(c);
    }

    public MagicList<T> withoutDuplicates() {
        return new MagicList<>(new HashSet<>(this));
    }

    @Override
    public Class<T> getComponentType() {
        return new GenericCapture<>(this).getType();
    }
    
    public JsonArray toJsonStringArray() {
        return JsonMapper.MAPPER.toJsonStringArray(this);
    }
    
    public JsonArray toJsonArray(Function<T, JsonElement> converter) {
        return JsonMapper.MAPPER.toJsonArray(this, converter);
    }
    
    public JsonArray toJsonArray() {
        return JsonMapper.MAPPER.toJsonArray(this);
    }

}
