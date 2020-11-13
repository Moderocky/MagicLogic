package mx.kenzie.magic.test;

import mx.kenzie.magic.collection.MagicArrayBackedList;
import mx.kenzie.magic.collection.MagicArrayList;
import org.junit.Test;

public class MagicArrayBackedGenericListTest {

    public static final int REMS = 100000;

    @Test
    public void main() {
        final MagicArrayBackedList<String> list = new MagicArrayBackedList<>();
        list.add("Hello");
        list.add("there!");
        list.add("General");
        list.add("Kenobi");
        assert list.entryCount() == 4;
        assert list.size() == MagicArrayBackedList.DEFAULT_SIZE;
        assert list.get(2).equals("General");
        list.trim();
        assert list.size() == 4;
        list.remove(1);
        list.add("there.");
        list.remove(3);
        list.add("Kenobi!");
        assert list.get(1).equals("there.");
        assert list.get(3).equals("Kenobi!");
        list.addAll("a", "b", "c", "d");
        assert list.size() == 8;
        assert list.get(7).equals("d");
        list.addAll("", "", "foo", "bar");
        assert list.size() == 12;
        assert list.get(11).equals("bar");
        list.removeIf(String::isEmpty);
        list.squash();
        assert list.size() == 10;
        assert list.get(9).equals("bar");
        list.add(4, "You");
        assert list.get(4).equals("You");
        list.addAll(5, "are", "a", "bold", "one...");
        assert list.get(7).equals("bold");
        final MagicArrayList<String> test = list.until(10);
        assert test.size() == 9;
        assert String.join(" ", test).equals("Hello there. General Kenobi! You are a bold one...");
        list.chop(9);
        assert list.size() == 9;
        assert String.join(" ", list).equals("Hello there. General Kenobi! You are a bold one...");
    }

}
