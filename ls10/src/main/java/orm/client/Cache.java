package orm.client;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Cache {
    private Map<Class, Set<Field>> cacheMap;

    public Cache() {
        this.cacheMap = new HashMap<>();
    }

    public Set<Field> getFieldsFromClass(final Class clazz) {
        if (!cacheMap.containsKey(clazz)) {
            cacheMap.put(clazz, getFieldSetTransitivity(clazz));
        }
        return cacheMap.get(clazz);
    }

    private Set<Field> getFieldSetTransitivity(final Class<?> clazz) {
        final Set<Field> fieldSet = new HashSet<>(Set.of(clazz.getDeclaredFields()));
        if (clazz.getSuperclass() != null) {
            fieldSet.addAll(Set.of(clazz.getSuperclass().getDeclaredFields()));
        }
        return fieldSet;
    }
}
