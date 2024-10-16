import java.lang.instrument.Instrumentation;

public class ObjectSizeFetcher {
    private static Instrumentation instrumentation;

    public static void premain(String args, Instrumentation inst) {
        instrumentation = inst;
    }

    public static long getObjectSize(Object object) {
        if (instrumentation == null) {
            throw new IllegalStateException("Instrumentation is not initialized");
        }
        return instrumentation.getObjectSize(object);
    }

    public static void main(String[] args) {
        // Example usage
        String exampleObject = "Hello, World!";
        System.out.println("Size of exampleObject: " + getObjectSize(exampleObject) + " bytes");
    }
}
