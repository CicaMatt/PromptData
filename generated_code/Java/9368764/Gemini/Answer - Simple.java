import java.lang.instrument.Instrumentation;

public class ObjectSizeEstimator {

    private static Instrumentation instrumentation;

    public static void premain(String agentArgs, Instrumentation inst) {
        instrumentation = inst;
    }

    public static long getObjectSize(Object object) {
        if (instrumentation == null) {
            throw new IllegalStateException("Agent not initialized. Make sure to include the agent in your JVM arguments.");
        }
        return instrumentation.getObjectSize(object);
    }
}