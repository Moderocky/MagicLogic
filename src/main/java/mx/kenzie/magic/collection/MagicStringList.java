package mx.kenzie.magic.collection;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.regex.Pattern;

public class MagicStringList extends MagicList<String> {

    public MagicStringList() {
        super();
    }

    public MagicStringList(final String... strings) {
        super(Arrays.asList(strings));
    }

    public MagicStringList(final Collection<? extends String> collection) {
        super(collection);
    }

    public MagicStringList(final Iterable<? extends String> iterable) {
        super(iterable);
    }

    public static MagicStringList from(final Collection<?> collection) {
        MagicStringList list = new MagicStringList();
        for (Object o : collection) {
            list.add(o.toString());
        }
        return list;
    }

    public static MagicStringList from(final Object... objects) {
        MagicStringList list = new MagicStringList();
        for (Object o : objects) {
            list.add(o.toString());
        }
        return list;
    }

    public MagicStringList filter(final Pattern pattern) {
        MagicStringList list = new MagicStringList();
        for (String string : this) {
            if (pattern.matcher(string).matches()) list.add(string);
        }
        return list;
    }

    public MagicStringList collectStrings(final Function<String, String> function) {
        return new MagicStringList(super.collect(function));
    }

    public <U> MagicStringList collectStrings(final BiFunction<String, U, String> biFunction, U argument) {
        return new MagicStringList(super.collect(biFunction, argument));
    }

    public void removeMatching(final Pattern pattern) {
        this.removeIf(string -> pattern.matcher(string).matches());
    }

    public void removeNotMatching(final Pattern pattern) {
        this.removeIf(string -> !pattern.matcher(string).matches());
    }

    public boolean containsIgnoreCase(final String string) {
        for (String s : this) {
            if (s.equalsIgnoreCase(string)) return true;
        }
        return false;
    }

    public boolean anyContains(final String string) {
        for (String s : this) {
            if (s.contains(string)) return true;
        }
        return false;
    }

    public double getMeanLength() {
        double sum = 0;
        for (String s : this) {
            sum += s.length();
        }
        return sum / size();
    }

    public String join(final CharSequence delimiter) {
        return String.join(delimiter, this);
    }

    public String join(final char delimiter) {
        return String.join(delimiter + "", this);
    }

    public MagicStringList withoutEmpty() {
        MagicStringList list = new MagicStringList(this);
        list.removeIf(string -> string.trim().isEmpty());
        return list;
    }

    @Override
    public MagicStringList addAnd(final String s) {
        return (MagicStringList) super.addAnd(s);
    }

    @Override
    public MagicStringList removeAnd(final String s) {
        return (MagicStringList) super.removeAnd(s);
    }

    @Override
    public MagicStringList removeAnd(int index) {
        return (MagicStringList) super.removeAnd(index);
    }

    @Override
    public MagicStringList shuffleAnd() {
        return (MagicStringList) super.shuffleAnd();
    }

    @Override
    public MagicStringList sort() {
        Collections.sort(this);
        return this;
    }

    @Override
    public MagicStringList from(int start) {
        return from(start, size());
    }

    @Override
    public MagicStringList from(int start, int end) {
        if (start < 0) throw new IllegalArgumentException("Start index must be positive!");
        if (end > size()) throw new IllegalArgumentException("End index must not be greater than the list's size!");
        MagicStringList list = new MagicStringList();
        for (int i = start; i < end; i++) {
            list.add(get(i));
        }
        return list;
    }

    @Override
    public Class<String> getComponentType() {
        return String.class;
    }

    @Override
    public String[] toArray() {
        return super.toArray(new String[0]);
    }

    @Override
    public String toString() {
        return String.join(" ", this);
    }

}
