import java.lang.instrument.Instrumentation;

public class ObjectSizeEstimator {

    private static Instrumentation instrumentation;

    public static void premain(String agentArgs, Instrumentation inst) {
        instrumentation = inst;
    }

    public static long getObjectSize(Object objectToSize) {
        if (instrumentation == null) {
            throw new IllegalStateException("Agent not initialized.");
        }
        return instrumentation.getObjectSize(objectToSize);
    }
}