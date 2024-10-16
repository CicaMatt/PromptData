import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class Processor implements Runnable {
    private CountDownLatch latch;

    public Processor(CountDownLatch latch) {
        this.latch = latch;
    }

    public void run() {
        System.out.println("Started.");

        try {
            Thread.sleep(3000);  // Simulate some work with sleep
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        latch.countDown();  // Decrement the latch count
        System.out.println("Thread completed, latch count: " + latch.getCount());
    }
}

public class App {

    public static void main(String[] args) {

        CountDownLatch latch = new CountDownLatch(3);  // Countdown starts from 3

        ExecutorService executor = Executors.newFixedThreadPool(3);  // 3 Threads in pool

        for(int i=0; i < 3; i++) {
            executor.submit(new Processor(latch));  // Submit tasks to thread pool
        }

        try {
            latch.await();  // Wait until latch reaches 0
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Completed.");  // All threads completed
        executor.shutdown();  // Shut down the executor
    }
}
