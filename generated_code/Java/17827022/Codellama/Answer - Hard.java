import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class App {
    public static void main(String[] args) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(3); // Create a latch with 3 countdowns

        ExecutorService executor = Executors.newFixedThreadPool(3); // Create a fixed-size thread pool of size 3

        for (int i = 0; i < 3; i++) {
            executor.submit(() -> {
                System.out.println("Started.");
                try {
                    Thread.sleep(3000); // Sleep for 3 seconds
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown(); // Decrement the countdown counter by 1
                }
            });
        }

        latch.await(); // Wait until the countdown reaches 0
        System.out.println("Completed.");
    }
}