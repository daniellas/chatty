package dl.chatty;

import static org.junit.Assert.*;

import java.io.File;

public class FileTestUtil {

    public static String absolutePath(File root) {
        return root.getAbsolutePath();
    }

    public static String createReadOnlyFolder(File root) {
        File ro = new File(root.getAbsolutePath() + "/ro");

        ro.mkdir();
        ro.setReadOnly();

        return "ro";
    }

    public static String createReadOnlyFile(File root, String path) {
        File ro = new File(root.getAbsolutePath() + "/" + path);

        ro.setReadOnly();

        return path;
    }

    public static void assertFolderExists(String path) {
        File file = new File(path);

        assertTrue(file.exists());
        assertTrue(file.isDirectory());
    }

    public static void assertFileExists(String path) {
        File file = new File(path);

        assertTrue(file.exists());
        assertTrue(file.isFile());
    }

}
