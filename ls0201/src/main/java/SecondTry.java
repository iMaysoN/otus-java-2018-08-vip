import com.carrotsearch.sizeof.RamUsageEstimator;

import java.util.ArrayList;
import java.util.function.Supplier;

public class SecondTry {
    private static final int SIZE = 100_000;

    private static <T> void generateAndPrint(final Supplier<T> supplier, final String comment) {
        final Object[] objs = new Object[SIZE];
        Runtime runtime = Runtime.getRuntime();
        runtime.gc();
        long memoryBefore = runtime.totalMemory() - runtime.freeMemory();
        for (int i = 0; i < SIZE; i++) {
            objs[i] = supplier.get();
        }
        runtime.gc();
        long memoryAfter = runtime.totalMemory() - runtime.freeMemory();
        System.out.println(String.format("%-30s\tSIZEOF(): %d/%d bytes%n",
                comment,
                Math.round((double) (memoryAfter - memoryBefore) / SIZE),
                Math.round((double) (RamUsageEstimator.sizeOf(objs)) / SIZE)));
    }

    public static void main(String[] args) throws InterruptedException {
        generateAndPrint(String::new, "Empty string");
        generateAndPrint(() -> "", "Empty string");
        generateAndPrint(InnerObject::new, "Inner object");
        generateAndPrint(() -> 52, "simple int");
        generateAndPrint(() -> true, "boolean");
        generateAndPrint(() -> false, "boolean");
        generateAndPrint(() -> new CompositeInnerObject1(52), "Composite object 1");
        generateAndPrint(() -> new CompositeInnerObject2("my tests"), "Composite object 2");
        generateAndPrint(() -> new ArrayList<Integer>(20), "Array of Integer objects");
    }

    private static class InnerObject {
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
