import java.util.ArrayList;
import java.util.Arrays;

public class MemoryExample {

    public static void printObjectSize(Object object) {
        System.out.println("Object type: " + object.getClass() +
                ", size: " + InstrumentationAgent.getObjectSize(object) + " bytes");
    }

    public static void main(String[] args) {

        InnerObject innerObject = new InnerObject();

        printObjectSize(innerObject);
        printObjectSize(new CompositeInnerObject1());
        printObjectSize(new CompositeInnerObject2());
        printObjectSize(new String());
        printObjectSize("Test string memory count");
        printObjectSize(new ArrayList<String>());
        printObjectSize(new ArrayList<String>(20));
        printObjectSize(new ArrayList<Integer>(20));
        printObjectSize(new ArrayList<Integer>() {{ addAll(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11)); }});
    }

    //java -XX:-UseCompressedOops --illegal-access=warn -javaagent:target/Agent.jar -jar target/Agent.jar
    //java -XX:-UseCompressedOops --illegal-access=warn -javaagent:target/Agent-jar-with-dependencies.jar -jar target/Agent-jar-with-dependencies.jar

    static class InnerObject {

    }

    static class CompositeInnerObject1 {
        private int first;
    }

    static class CompositeInnerObject2 {
        private String second;
    }
}
