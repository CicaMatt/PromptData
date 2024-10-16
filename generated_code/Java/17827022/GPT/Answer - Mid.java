import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class Processor implements Runnable {
    private CountDownLatch latch;

    public Processor(CountDownLatch latch) {
        this.latch = latch;
    }

    public void run() {
        System.out.println(Thread.currentThread().getName() + " started.");

        try {
            Thread.sleep(3000);  // Simulate task taking some time (3 seconds)
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        latch.countDown();  // Decrement latch count
        System.out.println(Thread.currentThread().getName() + " finished. Remaining latch count: " + latch.getCount());
    }
}

public class App {
    public static void main(String[] args) {
        CountDownLatch latch = new CountDownLatch(3);  // Countdown from 3 to 0
        ExecutorService executor = Executors.newFixedThreadPool(3);  // Thread pool with 3 threads

        // Submit 3 tasks to the executor, each using the same latch
        for (int i = 0; i < 3; i++) {
            executor.submit(new Processor(latch));
        }

        try {
            latch.await();  // Wait until latch has counted down to 0
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("All threads completed. Main program finished.");

        executor.shutdown();  // Properly shutdown the executor service
    }
}
