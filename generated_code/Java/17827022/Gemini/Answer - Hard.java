import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class Processor implements Runnable {
    private CountDownLatch latch;

    public Processor(CountDownLatch latch) {
        this.latch = latch;
    }

    public void run() {
        System.out.println("Started.");

        try {
            // Simulate work
            Thread.sleep(3000); 
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Preserve interrupt status
        } finally {
            latch.countDown(); 
        }
    }
}

public class App {

    public static void main(String[] args) {
        int numThreads = 3; 
        CountDownLatch latch = new CountDownLatch(numThreads); 

        ExecutorService executor = Executors.newFixedThreadPool(numThreads); 

        for (int i = 0; i < numThreads; i++) {
            executor.submit(new Processor(latch)); 
        }

        try {
            // Wait for all threads to finish, with a timeout for safety
            if (!latch.await(10, TimeUnit.SECONDS)) { // Adjust timeout as needed
                System.err.println("Timeout waiting for threads to complete.");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Preserve interrupt status
        } finally {
            executor.shutdownNow(); // Ensure cleanup even if timeout occurs
        }

        System.out.println("Completed.");
    }
}