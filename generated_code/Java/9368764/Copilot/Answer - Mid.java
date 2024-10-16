import java.lang.instrument.Instrumentation;

public class ObjectSizeFetcher {
    private static Instrumentation instrumentation;

    public static void premain(String args, Instrumentation inst) {
        instrumentation = inst;
    }

    public static long getObjectSize(Object obj) {
        if (instrumentation == null) {
            throw new IllegalStateException("Instrumentation is not initialized");
        }
        return instrumentation.getObjectSize(obj);
    }

    public static void main(String[] args) {
        // Example usage
        String example = "Hello, World!";
        System.out.println("Size of String object: " + getObjectSize(example) + " bytes");

        int[] intArray = new int[100];
        System.out.println("Size of int array: " + getObjectSize(intArray) + " bytes");
    }
}
