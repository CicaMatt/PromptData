import java.lang.instrument.Instrumentation;

public class ObjectSizeAgent {
    private static Instrumentation instrumentation;

    // This method is called before the main method when this class is passed as a Java agent
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

public class ObjectSizeTest {
    public static void main(String[] args) {
        // Example usage: measure the memory size of different objects
        String testString = "Hello, World!";
        int[] testArray = new int[1000];

        // Getting the size of the string object
        long stringSize = ObjectSizeAgent.getObjectSize(testString);
        System.out.println("Size of String object: " + stringSize + " bytes");

        // Getting the size of the integer array
        long arraySize = ObjectSizeAgent.getObjectSize(testArray);
        System.out.println("Size of int[] object: " + arraySize + " bytes");
    }
}
