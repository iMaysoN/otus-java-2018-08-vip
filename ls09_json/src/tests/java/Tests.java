import com.google.gson.Gson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class Tests {
    private static final Gson gson = new Gson();
    private static final IJson iJson = IJson.create();

    @Test
    void primitiveObject() throws IllegalAccessException {
        final TestObjectFactory.Primitives primitives = TestObjectFactory.getPrimitives();
        String gsonString = gson.toJson(primitives);
        String iJsonString = iJson.serializeObjectToJson(primitives);
        Assertions.assertEquals(gsonString, iJsonString, "expected Strings is equals");
    }

    @Test
    void nestedCollection() throws IllegalAccessException {
        final List<String> list1 = new ArrayList<>();
        final List<String> list2 = new ArrayList<>();
        list1.add("One");
        list2.add("Two");
        final List<List<String>> crazyList = new ArrayList<>();
        crazyList.add(list1);
        crazyList.add(list2);

        String gsonString = gson.toJson(crazyList);
        String iJsonString = iJson.serializeObjectToJson(crazyList);
        Assertions.assertEquals(gsonString, iJsonString, "expected Strings is equals");
    }
}
