package mx.kenzie.magic.collection;

import junit.framework.TestCase;

public class MagicListTest extends TestCase {

    public void testAddAnd() {
        assert new MagicList<>()
                .addAnd(new Object())
                .addAnd(new Object())
                .size() == 2;
    }

    public void testRemoveAnd() {
        assert new MagicList<>()
                .addAnd(new Object())
                .addAnd(new Object())
                .removeAnd(0)
                .removeAnd(0)
                .size() == 0;
    }

    public void testShuffleAnd() {
        assert new MagicList<>()
                .addAnd(new Object())
                .addAnd(new Object())
                .clone()
                .removeAnd(0)
                .removeAnd(0)
                .size() == 0;
    }

    public void testSort() {
    }

    public void testTestSort() {
    }

    public void testReverseSort() {
    }

    public void testSplit() {
    }
}