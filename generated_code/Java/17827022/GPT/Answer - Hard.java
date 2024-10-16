import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class Processor implements Runnable {
    private final CountDownLatch latch;

    public Processor(CountDownLatch latch) {
        this.latch = latch;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " started.");

        try {
            // Simulate task processing by sleeping for 3 seconds
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();  // Restore the interrupted status
            System.err.println("Thread was interrupted: " + e.getMessage());
        } finally {
            latch.countDown();  // Decrement the count of the latch
            System.out.println(Thread.currentThread().getName() + " finished. Latch count: " + latch.getCount());
        }
    }
}

public class App {

    public static void main(String[] args) {
        final int THREAD_COUNT = 3;
        CountDownLatch latch = new CountDownLatch(THREAD_COUNT);  // Latch initialized to 3

        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT); // Create a thread pool of 3 threads

        try {
            // Submit 3 tasks to the executor service
            for (int i = 0; i < THREAD_COUNT; i++) {
                executor.submit(new Processor(latch));
            }

            // Wait for the latch to count down to 0, or timeout after 10 seconds for safety
            if (!latch.await(10, TimeUnit.SECONDS)) {
                System.err.println("Latch countdown timed out.");
            } else {
                System.out.println("All tasks completed.");
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();  // Properly handle thread interruption
            System.err.println("Main thread was interrupted: " + e.getMessage());
        } finally {
            executor.shutdown();  // Properly shut down the executor service
            try {
                // Await termination of all threads in the executor service
                if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                    executor.shutdownNow();  // Force shutdown if tasks did not terminate in time
                }
            } catch (InterruptedException e) {
                executor.shutdownNow();
            }
        }

        System.out.println("Program completed.");
    }
}
