package victor.training.performance;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;

public class GlowrootUtil {
  static void deleteDatabase() throws IOException {
    String glowrootPath = ManagementFactory.getRuntimeMXBean().getInputArguments().stream()
            .filter(jvmArg -> jvmArg.startsWith("-javaagent:") && jvmArg.endsWith("glowroot.jar"))
            .map(jvmArg -> jvmArg.replace("-javaagent:", "").replace("glowroot.jar", ""))
            .findFirst()
            .orElse(null);
    if (glowrootPath == null) {
      System.out.println("glowroot agent not found");
      return;
    }
    System.out.println("Glowroot agent found running from path: " + glowrootPath);
    File dataDir = new File(glowrootPath + "data");
    if (dataDir.isDirectory()) {
      FileUtils.deleteDirectory(dataDir);
      System.out.println("glowroot db deleted");
    }
  }
}
