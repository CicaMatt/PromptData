import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class Processor implements Runnable {
    private final CountDownLatch latch;

    public Processor(CountDownLatch latch) {
        this.latch = latch;
    }

    @Override
    public void run() {
        System.out.println("Started.");

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore interrupted status
            System.err.println("Thread was interrupted");
        }

        latch.countDown();
    }
}

public class App {
    public static void main(String[] args) {
        CountDownLatch latch = new CountDownLatch(3); // Countdown from 3 to 0

        ExecutorService executor = Executors.newFixedThreadPool(3); // 3 Threads in pool

        for (int i = 0; i < 3; i++) {
            executor.submit(new Processor(latch)); // Reference to latch. Each time a new Processor is called, latch will count down by 1
        }

        try {
            latch.await(); // Wait until latch counts down to 0
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore interrupted status
            System.err.println("Main thread was interrupted");
        }

        System.out.println("Completed.");
        executor.shutdown(); // Properly shut down the executor
    }
}
