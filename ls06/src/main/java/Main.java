public class Main {
    public static void main(String[] args) throws InterruptedException {
        CacheEngine<Integer, String> cacheEngine = new CacheEngine<>(5, 4000);
        cacheEngine.store(1, "one");
        Thread.sleep(1000);
        cacheEngine.store(2, "two");
        Thread.sleep(1000);
        cacheEngine.store(3, "three");
        Thread.sleep(1000);

        System.out.printf("Current cache size: %s.%n", cacheEngine.size());
        for (int i = 1; i < 4; i++) {
            System.out.printf("Element '%s' is '%s'.%n", i, cacheEngine.get(i));
        }
        System.out.printf("Current cache size: %s.%n", cacheEngine.size());
        Thread.sleep(1000);
        System.out.printf("Current cache size: %s. Expected: 2.%n", cacheEngine.size());
        cacheEngine.store(4, "four");
        System.out.printf("Current cache size: %s. Expected: 3.%n", cacheEngine.size());
        Thread.sleep(5000);
        System.out.printf("Current cache size: %s. Expected: 0.%n", cacheEngine.size());
        System.out.printf("Get unexisted element: %s.%n", cacheEngine.get(4));
        cacheEngine.close();
    }
}
