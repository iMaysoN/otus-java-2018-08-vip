package orm.client;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Cache {
    private static String INSERT_BLUEPRINT = "insert into orm (%s) values (%s)";

    private Map<Class, Set<Field>> cacheMap;
    private Map<Class, String> cachedInsertRequests;

    public Cache() {
        this.cacheMap = new HashMap<>();
        this.cachedInsertRequests = new HashMap<>();
    }

    public Set<Field> getFieldsFromClass(final Class clazz) {
        if (!cacheMap.containsKey(clazz)) {
            cacheMap.put(clazz, getFieldSetTransitivity(clazz));
        }
        return cacheMap.get(clazz);
    }

    public String getInsertRequestForObject(final Object object) {
        if (!cachedInsertRequests.containsKey(object.getClass())) {
            cacheInsertRequest(object);
        }
        return cachedInsertRequests.get(object.getClass());
    }

    private void cacheInsertRequest(final Object object) {
        final ArrayList<String> fieldNames = new ArrayList<>();
        final Set<Field> fields = getFieldsFromClass(object.getClass());
        for (final Field field : fields) {
            fieldNames.add(field.getName());
        }
        final String insert = String.format(INSERT_BLUEPRINT,
                String.join(", ", fieldNames),
                Stream.generate(() -> "?").limit(fieldNames.size()).collect(Collectors.joining(", ")));
        cachedInsertRequests.put(object.getClass(), insert);
    }

    private Set<Field> getFieldSetTransitivity(final Class<?> clazz) {
        final Set<Field> fieldSet = new HashSet<>(Set.of(clazz.getDeclaredFields()));
        if (clazz.getSuperclass() != null) {
            fieldSet.addAll(Set.of(clazz.getSuperclass().getDeclaredFields()));
        }
        return fieldSet;
    }
}
