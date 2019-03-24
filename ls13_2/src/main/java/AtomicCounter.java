import java.util.concurrent.atomic.AtomicInteger;

public class AtomicCounter {
    public static void main(String[] args) {
        AtomicInteger counter1 = new AtomicInteger(0);
        AtomicInteger counter2 = new AtomicInteger(0);

        new Thread(new ThreadPrinter("Поток 1") {
            @Override
            boolean checkCounters(AtomicInteger counter1, AtomicInteger counter2) {
                return counter1.get() == counter2.get();
            }

            @Override
            public void run() {
                while (true) {
                    if (checkCounters(counter1, counter2)) {
                        incrementOrDecrementCounter(counter1);

                    }
                }
            }
        }).start();
        new Thread(new ThreadPrinter("Поток 2") {
            @Override
            boolean checkCounters(AtomicInteger counter1, AtomicInteger counter2) {
                return counter1.get() != counter2.get();
            }

            @Override
            public void run() {
                while (true) {
                    if (checkCounters(counter1, counter2)) {
                        incrementOrDecrementCounter(counter2);
                    } else {
                        sleep(10);
                    }
                }
            }
        }).start();
    }

    abstract static class ThreadPrinter implements Runnable {
        private String threadName;
        private String chainString = "";
        private int mode = 1;

        ThreadPrinter(String threadName) {
            this.threadName = threadName;
        }

        abstract boolean checkCounters(AtomicInteger counter1, AtomicInteger counter2);
        abstract public void run();

        void incrementOrDecrementCounter(AtomicInteger counter) {
            if (threadName.equals("Поток 1")) {
                sleep(1000);
            }
            if (mode > 0) {
                counter.incrementAndGet();
            } else {
                counter.decrementAndGet();
            }

            if (counter.get() == 10) {
                mode = -1;
            } else if (counter.get() == 1) {
                mode = 1;
            }

            chainString = chainString.isEmpty()
                    ? "" + counter.get()
                    : String.join(", ", chainString, String.valueOf(counter.get()));

            System.out.println(threadName + ": " + chainString);
        }
    }

    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
