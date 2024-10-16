import java.lang.instrument.Instrumentation;

public class ObjectSizeFetcher {

    private static Instrumentation instrumentation;

    // This method is called automatically by the JVM when the agent is attached.
    public static void premain(String args, Instrumentation inst) {
        instrumentation = inst;
    }

    // Method to get the size of an object
    public static long getObjectSize(Object object) {
        if (instrumentation == null) {
            throw new IllegalStateException("Instrumentation is not initialized.");
        }
        return instrumentation.getObjectSize(object);
    }

    public static void main(String[] args) {
        // Example usage
        String testString = "Hello, World!";
        int[] testArray = new int[1000];

        System.out.println("Size of String: " + getObjectSize(testString) + " bytes");
        System.out.println("Size of int[1000]: " + getObjectSize(testArray) + " bytes");
    }
}
