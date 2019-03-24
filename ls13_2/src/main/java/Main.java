import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

public class Main {
    public static void main(String[] args) {
        final AtomicInteger counter = new AtomicInteger(0);
        Function<String, Runnable> func = threadName -> new Runnable() {
            private String message = "";
            private int mode = 1;

            @Override
            public void run() {
                while (true) {
                    if (threadName.equals("Поток 1")) {
                        int currentValue = counter.get();
                        if (currentValue == 10) {
                            mode = -1;
                        } else if (currentValue == 1) {
                            mode = 1;
                        }
                        currentValue = mode > 0
                                ? currentValue + 1
                                : currentValue - 1;

                        message = message.isEmpty()
                                ? String.valueOf(currentValue)
                                : String.join(", ", message, String.valueOf(currentValue));
                        System.out.println(threadName + ": " + message);
                        counter.set(currentValue);
                        sleep(1000);
                    } else if (threadName.equals("Поток 2")) {
                        int currentValue = counter.get();
                        if (currentValue != 0 && !message.endsWith(String.valueOf(currentValue))) {
                            message = message.isEmpty()
                                    ? String.valueOf(currentValue)
                                    : String.join(", ", message, String.valueOf(currentValue));
                            System.out.println(threadName + ": " + message);
                        }
                    }
                }
            }
        };

        new Thread(func.apply("Поток 1")).start();
        new Thread(func.apply("Поток 2")).start();
    }

    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
