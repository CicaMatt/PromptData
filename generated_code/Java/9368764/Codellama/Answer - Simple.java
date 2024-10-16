import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;

public class MemorySize {
    public static void main(String[] args) {
        // Replace with your object here
        Object obj = new Object(); 
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage heapMemoryUsage = memoryMXBean.getHeapMemoryUsage();
        long initialMemory = heapMemoryUsage.getInit();
        long usedMemory = heapMemoryUsage.getUsed();
        System.out.println("Initial Memory: " + initialMemory);
        System.out.println("Used Memory: " + usedMemory);
    }
}