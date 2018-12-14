import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TestObjectFactory {
    public static Primitives getPrimitives() {
        return new Primitives();
    }

    public static class Primitives {
        private boolean thisIsTrue = true;
        private transient int transientInt = 17;
        private int invValue = 100500;
        public int publicInValue = 100501;
        private int[] intArray = new int[]{10, 20, 30, 40, 50, 60, 70};
        private double doubleValue = 17.17;
        private long longValue = 100L;
        private Integer integerVaue = 42;
        private List<Integer> listInteger = Arrays.asList(10, 20, 30);
        private String someString = "this is SPARTA!";
        private char someChar = 'A';
        private Set<String> setOfString = Set.of("one", "two", "thre");
        private Map<Integer, String> mapIntToString = Map.of(10, "one", 20, "two");
        private SmallPrimitives smallPrimitives = new SmallPrimitives();
    }

    private static class SmallPrimitives {
        private int invValue = 201000;
        private Integer integerVaue = 84;
    }
}
