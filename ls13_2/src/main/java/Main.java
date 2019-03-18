import java.util.function.Function;

public class Main {

    public static void main(String[] args) {
        Function<String, Runnable> func = threadName -> new Runnable() {
            private String message = threadName + ": ";
            private int counter = 0;
            private int mode = 1;

            @Override
            public void run() {
                while (true) {
                    if (counter == 0) {
                        counter = counter + mode;
                        message = message + counter;
                    } else {
                        counter = counter + mode;
                        message = String.join(", ", message, String.valueOf(counter));
                    }
                    if (counter == 10) mode = -1;
                    if (counter == 1) mode = 1;

                    System.out.println(message);
                    sleep(1000);
                }
            }
        };

        new Thread(func.apply("Поток 1")).start();
        sleep(100);
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
