import java.lang.instrument.Instrumentation;
import java.util.*;

class MemoryUtil {
  private static Instrumentation instrumentation = null;

  public static void premain(String agentArgs, Instrumentation inst) {
    instrumentation = inst;
  }

  public static long getObjectSize(Object object) {
    if (instrumentation == null) {
      return -1;
    }

    return instrumentation.getObjectSize(object);
  }
}

public class ObjectSizer {
  public static void main(String[] args) {
    // Create an object of some type
    Object object = new Object();

    // Get the size of the object in bytes
    long sizeInBytes = MemoryUtil.getObjectSize(object);

    System.out.println("The object takes up " + sizeInBytes + " bytes");
  }
}