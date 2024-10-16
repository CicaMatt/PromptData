import java.util.concurrent.CountDownLatch;
 
class Processor implements Runnable {
    private CountDownLatch latch;
    
    public Processor(CountDownLatch latch) {
        this.latch = latch;
    }
    
    public void run() {
        System.out.println("Started.");
        
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        latch.countDown();
    }
}

public class App {
    public static void main(String[] args) {
        
        CountDownLatch latch = new CountDownLatch(3); // coundown from 3 to 0
        
        ExecutorService executor = Executors.newFixedThreadPool(3); // 3 Threads in pool
        
        for(int i=0; i < 3; i++) {
            executor.submit(new Processor(latch)); // ref to latch. each time call new Processes latch will count down by 1
        }
        
        try {
            latch.await();  // wait until latch counted down to 0
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        System.out.println("Completed.");
    }
}