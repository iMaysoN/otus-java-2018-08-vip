import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static java.lang.reflect.Modifier.isTransient;

public class IJson {

    private IJson() {
    }

    public static IJson create() {
        return new IJson();
    }

    public String serializeObjectToJson(final Object object) {
        return convertSimpleObjectToJson(object);
    }

    public String convertComplexObjectToJson(final Object object) throws IllegalAccessException {
        final Class<?> clazz = object.getClass();
        final Field[] fields = clazz.getDeclaredFields();
        final List<String> listResult = new ArrayList<>();

        for (final Field field : fields) {
            if (!isTransient(field.getModifiers())) {
                field.setAccessible(true);
                if (field.get(object) != null) {
                    listResult.add(
                            String.format("\"%s\":%s",
                                    field.getName(),
                                    convertSimpleObjectToJson(field.get(object)))
                    );
                }
            }
        }
        return asStructure(listResult);
    }

    private String convertSimpleObjectToJson(final Object singleObject) {
        if (singleObject == null) {
            return "";
        } else if (singleObject instanceof Collection) {
            return convertCollection((Collection) singleObject);
        } else if (singleObject instanceof Map) {
            return convertMap((Map) singleObject);
        } else if (singleObject instanceof Character || singleObject instanceof String) {
            return String.format("\"%s\"", singleObject.toString());
        } else if (singleObject instanceof Boolean
                || singleObject instanceof Number
                || singleObject.getClass().isPrimitive()) {
            return singleObject.toString();
        } else if (singleObject.getClass().isArray()) {
            return convertArray(singleObject);
        }
        try {
            return convertComplexObjectToJson(singleObject);
        } catch (IllegalAccessException e) {
            return "";
        }
    }

    private String convertCollection(final Collection<?> collectionObject) {
        final List<String> convertedResults = new ArrayList<>();
        collectionObject.iterator()
                .forEachRemaining(next -> convertedResults.add(convertSimpleObjectToJson(next)));
        return asElement(convertedResults);
    }

    private String convertMap(final Map mapObject) {
        final List<String> convertedResult = new ArrayList<>();
        mapObject.forEach((key, value) -> convertedResult.add(
                String.format("%s:%s", convertSimpleObjectToJson(key instanceof Number ? key.toString() : key), convertSimpleObjectToJson(value))
        ));
        return asStructure(convertedResult);
    }

    private String convertArray(final Object array) {
        final List<String> convertedResult = new ArrayList<>();
        for (int i = 0; i < Array.getLength(array); i++) {
            final Object objectFromArray = Array.get(array, i);
            convertedResult.add(convertSimpleObjectToJson(objectFromArray));
        }
        return asElement(convertedResult);
    }

    private String asElement(final List<String> results) {
        return String.format("[%s]", String.join(",", results));
    }

    private String asStructure(final List<String> results) {
        return String.format("{%s}", String.join(",", results));
    }
}
