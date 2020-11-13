package mx.kenzie.magic.test;

import mx.kenzie.magic.collection.MagicArrayBackedList;
import mx.kenzie.magic.magic.GenericCapture;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class GenericCaptureTest {

    @Test
    public void main() {
        Class<?> cls = new GenericCapture<>(new ArrayList<String>()).getType();
        assert cls == String.class;

        List<String> list = new ArrayList<>();
        list.add("hi");
        Class<?> cls2 = new GenericCapture<>(list).getType();
        assert cls2 == String.class;

        assert new MagicArrayBackedList<>("hello").getComponentType() == String.class;
        assert new MagicArrayBackedList<>(66, 2).getComponentType() == Integer.class;
    }


}
