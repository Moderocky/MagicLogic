package mx.kenzie.magic.magic;

import com.google.gson.*;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;

public class JsonMapper {
    
    public static final boolean JSON_ALLOWED;
    public static final JsonMapper MAPPER = new JsonMapper();
    
    static {
        boolean allowed;
        try {
            allowed = Class.forName("com.google.gson.JsonObject") != null;
        } catch (ClassNotFoundException e) {
            allowed = false;
        }
        JSON_ALLOWED = allowed;
    }
    
    public <T> JsonArray toJsonStringArray(Collection<T> list) {
        JsonArray array = new JsonArray();
        for (T thing : list) {
            array.add(thing.toString());
        }
        return array;
    }
    
    public <T> JsonArray toJsonArray(Collection<T> list, Function<T, JsonElement> converter) {
        JsonArray array = new JsonArray();
        for (T thing : list) {
            array.add(converter.apply(thing));
        }
        return array;
    }
    
    public <T> JsonArray toJsonArray(Collection<T> list) {
        Gson gson = new Gson();
        JsonArray array = new JsonArray();
        for (T thing : list) {
            if (thing instanceof JsonElement)
                array.add((JsonElement) thing);
            else if (thing instanceof String)
                array.add(thing.toString());
            else if (thing instanceof Integer)
                array.add((Integer) thing);
            else if (thing instanceof Number)
                array.add((Number) thing);
            else if (thing instanceof Boolean)
                array.add((Boolean) thing);
            else
                array.add(gson.toJsonTree(thing));
        }
        return array;
    }
    
    public <K, V> JsonObject toJsonObject(Map<K, V> map) {
        Gson gson = new Gson();
        JsonObject object = new JsonObject();
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (entry.getValue() instanceof String)
                object.add(entry.getKey().toString(), new JsonPrimitive(entry.getValue().toString()));
            else if (entry.getValue() instanceof Integer)
                object.add(entry.getKey().toString(), new JsonPrimitive((Integer) entry.getValue()));
            else if (entry.getValue() instanceof Number)
                object.add(entry.getKey().toString(), new JsonPrimitive((Number) entry.getValue()));
            else if (entry.getValue() instanceof Boolean)
                object.add(entry.getKey().toString(), new JsonPrimitive((Boolean) entry.getValue()));
            else
                object.add(entry.getKey().toString(), gson.toJsonTree(entry.getValue()));
        }
        return object;
    }
    
    public <K, V> JsonObject toJsonObject(Map<K, V> map, Function<V, JsonElement> converter) {
        JsonObject object = new JsonObject();
        for (Map.Entry<K, V> entry : map.entrySet()) {
            object.add(entry.getKey().toString(), converter.apply(entry.getValue()));
        }
        return object;
    }
    
}
