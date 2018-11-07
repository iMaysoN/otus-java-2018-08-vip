import com.carrotsearch.sizeof.RamUsageEstimator;

import java.util.ArrayList;

public class SecondTry {

    public static void main(String[] args) throws InterruptedException {
        long mem0 = currentMemory();
        System.out.printf("Memory at the beginning: %s%n", mem0);

        String simpleString = "";
        long mem1 = currentMemory();
        System.out.printf("Size of '%s': %s [%s].%n", "simple string", mem1 - mem0, RamUsageEstimator.sizeOf(simpleString));

        String newString = new String("");
        long mem2 = currentMemory();
        System.out.printf("Size of '%s': %s [%s].%n", "new string", mem2 - mem1, RamUsageEstimator.sizeOf(newString));

        InnerObject innerObject = new InnerObject();
        long mem3 = currentMemory();
        System.out.printf("Size of '%s': %s [%s].%n", "inner object", mem3 - mem2, RamUsageEstimator.sizeOf(innerObject));

        CompositeInnerObject1 compositeInnerObject1 = new CompositeInnerObject1();
        long mem4 = currentMemory();
        System.out.printf("Size of '%s': %s [%s].%n", "composite with int", mem4 - mem3, RamUsageEstimator.sizeOf(compositeInnerObject1));

        CompositeInnerObject2 compositeInnerObject2 = new CompositeInnerObject2();
        long mem5 = currentMemory();
        System.out.printf("Size of '%s': %s [%s].%n", "composite with string", mem5 - mem4, RamUsageEstimator.sizeOf(compositeInnerObject2));

        ArrayList<Integer> integers = new ArrayList<>(20);
        long mem6 = currentMemory();
        System.out.printf("Size of '%s': %s [%s].%n", "array list of int", mem6 - mem5, RamUsageEstimator.sizeOf(integers));
    }

    private static long currentMemory() throws InterruptedException {
        System.gc();
        Thread.sleep(10);
        Runtime runtime = Runtime.getRuntime();
        return runtime.totalMemory() - runtime.freeMemory();
    }

    static class InnerObject {
    }

    static class CompositeInnerObject1 {
        private int first;
    }

    static class CompositeInnerObject2 {
        private String second;
    }
}
