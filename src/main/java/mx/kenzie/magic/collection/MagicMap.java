package mx.kenzie.magic.collection;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Predicate;

public class MagicMap<K, V> extends LinkedHashMap<K, V> {

    public MagicMap() {
        super();
    }

    public MagicMap(Map<K, V> map) {
        super(map);
    }

    public MagicMap(Collection<K> keys, Collection<V> values) {
        Object[] ks = keys.toArray();
        Object[] vs = values.toArray();
        for (int i = 0; i < Math.min(ks.length, vs.length); i++) {
            put((K) ks[i], (V) vs[i]);
        }
    }

    public MagicMap(K[] ks, V[] vs) {
        for (int i = 0; i < Math.min(ks.length, vs.length); i++) {
            put(ks[i], vs[i]);
        }
    }

    public static <V> MagicMap<Integer, V> ofIndices(Collection<V> collection) {
        MagicMap<Integer, V> map = new MagicMap<>();
        int i = 0;
        for (V v : collection) {
            map.put(i, v);
            i++;
        }
        return map;
    }

    public boolean removeEntryIf(Predicate<? super Map.Entry<? super K, ? super V>> filter) {
        return entrySet().removeIf(filter);
    }

    public Map.Entry<K, V> remove(int position) {
        if (position >= size()) throw new IllegalArgumentException("Index is out of bounds!");
        int i = 0;
        for (K k : new HashSet<>(keySet())) {
            if (i == position) {
                final V v = get(k);
                remove(k);
                return new Map.Entry<K, V>() {
                    @Override
                    public K getKey() {
                        return k;
                    }

                    @Override
                    public V getValue() {
                        return v;
                    }

                    @Override
                    public V setValue(V value) {
                        return null;
                    }
                };
            }
            i++;
        }
        throw new IllegalArgumentException("Index is out of bounds!");
    }

    public Map.Entry<K, V> getFirst() {
        return get(0);
    }

    public Map.Entry<K, V> get(int position) {
        if (position >= size() || position < 0) throw new IllegalArgumentException("Index is out of bounds!");
        int i = 0;
        for (K k : keySet()) {
            if (i == position) {
                return new Map.Entry<K, V>() {
                    @Override
                    public K getKey() {
                        return k;
                    }

                    @Override
                    public V getValue() {
                        return get(k);
                    }

                    @Override
                    public V setValue(V value) {
                        return replace(k, value);
                    }
                };
            }
            i++;
        }
        throw new IllegalArgumentException("Index is out of bounds!");
    }

    public Map.Entry<K, V> getLast() {
        return get(size() - 1);
    }

    public boolean contains(Object o) {
        return this.containsValue(o) || this.containsKey(o);
    }

    @NotNull
    public Iterator<Map.Entry<K, V>> iterator() {
        return this.entrySet().iterator();
    }

    @NotNull
    public Object[] toArray() {
        return this.entrySet().toArray();
    }

    @NotNull
    @Deprecated
    public <T> T[] toArray(@NotNull T[] a) {
        return this.entrySet().toArray(a);
    }

    public boolean add(Map.Entry<K, V> kvEntry) {
        return this.entrySet().add(kvEntry);
    }

    public boolean containsAll(@NotNull Collection<?> c) {
        return this.entrySet().containsAll(c);
    }

    public boolean addAll(@NotNull Collection<? extends Map.Entry<K, V>> c) {
        return this.entrySet().addAll(c);
    }

    public boolean removeAll(@NotNull Collection<?> c) {
        return this.entrySet().removeAll(c);
    }

    public boolean retainAll(@NotNull Collection<?> c) {
        return this.entrySet().retainAll(c);
    }
}
