import java.util.concurrent.atomic.AtomicInteger;

public class AtomicCounter {
    public static void main(String[] args) {
        AtomicInteger counter1 = new AtomicInteger(0);
        AtomicInteger counter2 = new AtomicInteger(0);

        new Thread(new Runnable() {
            private String threadName = "Поток 1";
            private String chainString = "";
            private int mode = 1;

            @Override
            public void run() {
                while (true) {
                    if (counter1.get() == counter2.get()) {
                        if (mode > 0) {
                            counter1.incrementAndGet();
                        } else {
                            counter1.decrementAndGet();
                        }
                        if (counter1.get() == 10) {
                            mode = -1;
                        } else if (counter1.get() == 1) {
                            mode = 1;
                        }
                        if (chainString.isEmpty()) {
                            chainString = "" + counter1.get();
                        } else {
                            chainString = String.join(", ", chainString, String.valueOf(counter1.get()));
                        }
                        System.out.println(threadName + ": " + chainString);
                        sleep(1000);
                    }

                }
            }

        }).start();

        new Thread(new Runnable() {
            private String threadName = "Поток 2";
            private String chainString = "";
            private int mode = 1;

            @Override
            public void run() {
                while (true) {
                    if (counter1.get() != counter2.get()) {
                        if (mode > 0) {
                            counter2.incrementAndGet();
                        } else {
                            counter2.decrementAndGet();
                        }

                        if (counter2.get() == 10) {
                            mode = -1;
                        } else if (counter2.get() == 1) {
                            mode = 1;
                        }

                        if (chainString.isEmpty()) {
                            chainString = "" + counter2.get();
                        } else {
                            chainString = String.join(", ", chainString, String.valueOf(counter2.get()));
                        }
                        System.out.println(threadName + ": " + chainString);
                        sleep(1000);
                    } else {
                        sleep(10); //give thread 1 time to increased
                    }
                }
            }
        }).start();
    }

    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
