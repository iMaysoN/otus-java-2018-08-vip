import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        long startedTime = System.currentTimeMillis();

        ArrayList<Inside> myList = new ArrayList<>();

        while (true) {
            try {
                for (int j = 0; j < 70000; j++) {
                    myList.add(new Inside("Kavabanga!"));
                }
                for (int i = 0; i < 70000 / 2; i++) {
                    myList.remove(myList.size() - 1);
                }
                runGc();
            } catch (OutOfMemoryError e) {
                System.out.printf("ERROR: %s%n", e.getMessage());
                System.out.printf("Live time: %s ms.%n", (System.currentTimeMillis() - startedTime));
                System.out.printf("Global result: Young - %s/%s, Old - %s/%s.%n", globalYoungCount, globalYoungTime, globalOldCount, globalOldTime);
                return;
            }
        }
    }

    private static long globalYoungTime = 0;
    private static long globalYoungCount = 0;
    private static long globalOldTime = 0;
    private static long globalOldCount = 0;

    private static void runGc() {
        long youngTime = 0;
        long youngCount = 0;
        long oldTime = 0;
        long oldCount = 0;
        long unknownTime = 0;
        long unknownCount = 0;

        List<GarbageCollectorMXBean> garbageCollectors = ManagementFactory.getGarbageCollectorMXBeans();
        for (GarbageCollectorMXBean gc : garbageCollectors) {
            String gcName = gc.getName();
            long gcTime = gc.getCollectionTime();
            long gcCount = gc.getCollectionCount();
            if ("G1 Young Generation".contains(gcName)) {
                youngCount += gcCount;
                youngTime += gcTime;
            } else if ("G1 Old Generation".contains(gcName)) {
                oldCount += gcCount;
                oldTime += gcTime;
            } else {
                unknownCount += gcCount;
                unknownTime += gcTime;
            }
            System.out.printf("Current GC - %s, Collection count - %s, Time - %s%n", gcName, gcCount, gcTime);
        }
        globalOldCount += oldCount;
        globalOldTime += oldTime;
        globalYoungCount += youngCount;
        globalYoungTime += youngTime;
        System.out.printf("Young - %s/%s, Old - %s/%s, Alien - %s/%s.%n", youngCount, youngTime, oldCount, oldTime, unknownCount, unknownTime);
    }

    static class Inside {
        private String insideInside;

        Inside(String insideInside) {
            this.insideInside = insideInside;
        }
    }
}
