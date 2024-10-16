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
            Thread.sleep(3000); // Simulate some work
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        latch.countDown(); // Signal completion of this task
    }
}

public class App {

    public static void main(String[] args) {

        CountDownLatch latch = new CountDownLatch(3); 

        ExecutorService executor = Executors.newFixedThreadPool(3);

        for(int i=0; i < 3; i++) {
            executor.submit(new Processor(latch)); 
        }

        try {
            latch.await(); // Main thread waits here until all 3 tasks are done
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Completed."); // This line executes only after all tasks are done
    }

}