package yoshikihigo.jcf;

import java.io.File;
import java.util.SortedSet;
import java.util.TreeSet;

public class FileUtility {

  public static SortedSet<File> getFiles(final File file) {

    final SortedSet<File> files = new TreeSet<>();

    if (file.isDirectory()) {
      final File[] children = file.listFiles();
      if (null != children) {
        for (final File child : children) {
          files.addAll(getFiles(child));
        }
      }
    }

    else if (file.isFile()) {
      if (file.getName()
          .endsWith(".java")) {
        files.add(file);
      }
    }

    else {
      System.err.println("\"" + file.getAbsolutePath() + "\" is not a vaild file!");
    }

    return files;
  }
}
