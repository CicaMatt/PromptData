import java.lang.instrument.Instrumentation;

public class ObjectSizeEstimator {

    private static Instrumentation instrumentation;

    public static void premain(String agentArgs, Instrumentation inst) {
        instrumentation = inst;
    }

    public static long getObjectSize(Object object) {
        if (instrumentation == null) {
            throw new IllegalStateException("Agent not initialized. Make sure to include the agent as a VM argument.");
        }
        return instrumentation.getObjectSize(object);
    }

    public static void main(String[] args) {
        // Example usage:

        // Make sure to include the agent when running your program:
        // java -javaagent:path/to/your/agent.jar YourMainClass

        int[] largeArray = new int[1000000]; 

        long sizeInBytes = getObjectSize(largeArray);
        System.out.println("Size of largeArray: " + sizeInBytes + " bytes");
    }
}