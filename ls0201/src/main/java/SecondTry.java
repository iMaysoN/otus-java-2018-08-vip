import java.util.ArrayList;

public class SecondTry {
    private static ArrayList<Long> memoryList = new ArrayList<>();
    private static int index = 0;

    public static void main(String[] args) throws InterruptedException {
        memoryList.add(currentMemory());
        System.out.printf("Memory at the beginning: %s%n", memoryList.get(index));

        String simpleString = "";
        addMemoryValueToCacheAndPrint("simple String");

        String newString = new String("");
        addMemoryValueToCacheAndPrint("new String");

        InnerObject innerObject = new InnerObject();
        addMemoryValueToCacheAndPrint("inner object");

        CompositeInnerObject1 compositeInnerObject1 = new CompositeInnerObject1();
        addMemoryValueToCacheAndPrint("composite object with int");

        CompositeInnerObject1 compositeInnerObject2 = new CompositeInnerObject1();
        addMemoryValueToCacheAndPrint("composite object with string");

        ArrayList<Integer> integers = new ArrayList<>(20);
        addMemoryValueToCacheAndPrint("array of int");
    }

    private static long currentMemory() throws InterruptedException {
        System.gc();
        Thread.sleep(1000);
        Runtime runtime = Runtime.getRuntime();
        return runtime.totalMemory() - runtime.freeMemory();
    }

    private static void addMemoryValueToCacheAndPrint(String objectName) throws InterruptedException {
        memoryList.add(currentMemory());
        index++;
        System.out.printf("Memory after %s - %s%nMemory for %s - %s%n",
                objectName, memoryList.get(index), objectName, (memoryList.get(index) - memoryList.get(index-1)));
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
