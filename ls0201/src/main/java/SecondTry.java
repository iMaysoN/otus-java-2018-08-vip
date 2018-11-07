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

        CompositeInnerObject1 compositeInnerObject1 = new CompositeInnerObject1(42);
        long mem4 = currentMemory();
        System.out.printf("Size of '%s': %s [%s].%n", "composite with int", mem4 - mem3, compositeInnerObject1.memory());

        CompositeInnerObject2 compositeInnerObject2 = new CompositeInnerObject2("Da ba di");
        long mem5 = currentMemory();
        compositeInnerObject2.memory();
        System.out.printf("Size of '%s': %s [%s].%n", "composite with string", mem5 - mem4, compositeInnerObject2.memory());

        ArrayList<Integer> integers = new ArrayList<>(20);
        long mem6 = currentMemory();
        System.out.printf("Size of '%s': %s [%s].%n", "array list of int", mem6 - mem5, RamUsageEstimator.sizeOf(integers));

        int int1 = 42;
        long mem7 = currentMemory();
        System.out.printf("Size of '%s': %s [%s].%n", "int", mem7 - mem6, RamUsageEstimator.sizeOf(int1));

        Integer int2 = Integer.valueOf(42);
        long mem8 = currentMemory();
        System.out.printf("Size of '%s': %s [%s].%n", "Integer", mem8 - mem7, RamUsageEstimator.sizeOf(int2));
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
        int first;

        CompositeInnerObject1(int first) {
            this.first = first;
        }

        long memory() {
            return RamUsageEstimator.sizeOf(this);
        }
    }

    public static class CompositeInnerObject2 {
        String second;

        CompositeInnerObject2(String second) {
            this.second = second;
        }

        long memory() {
            return RamUsageEstimator.sizeOf(this);
        }
    }
}
