import java.lang.instrument.Instrumentation;

public class ObjectSizeAgent {

    private static Instrumentation instrumentation;

    // This method is called when the agent is loaded
    public static void premain(String args, Instrumentation inst) {
        instrumentation = inst;
    }

    public static long getObjectSize(Object obj) {
        if (instrumentation == null) {
            throw new IllegalStateException("Agent not initialized.");
        }
        return instrumentation.getObjectSize(obj);
    }
}

public class Main {
    public static void main(String[] args) {
        // Launch the JVM with the agent
        try {
            String jarPath = "path_to/ObjectSizeAgent.jar";
            java.lang.management.ManagementFactory.getRuntimeMXBean()
                    .getInputArguments()
                    .add("-javaagent:" + jarPath);
        } catch (Exception e) {
            System.err.println("Failed to load the agent: " + e.getMessage());
        }

        // Create some objects
        String str = "Hello, World!";
        int[] array = new int[1000];

        // Measure the memory size of objects
        long strSize = ObjectSizeAgent.getObjectSize(str);
        long arraySize = ObjectSizeAgent.getObjectSize(array);

        System.out.println("Size of String object: " + strSize + " bytes");
        System.out.println("Size of int array: " + arraySize + " bytes");
    }
}
