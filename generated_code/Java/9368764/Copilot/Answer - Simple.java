import java.lang.instrument.Instrumentation;

public class ObjectSizeFetcher {
    private static Instrumentation instrumentation;

    public static void premain(String args, Instrumentation inst) {
        instrumentation = inst;
    }

    public static long getObjectSize(Object obj) {
        return instrumentation.getObjectSize(obj);
    }

    public static void main(String[] args) {
        // Example usage
        String example = "Hello, World!";
        System.out.println("Size of example string: " + getObjectSize(example) + " bytes");
    }
}
