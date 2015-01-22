package mi.practice.java.eight.lambda;

import java.io.File;
import java.io.FileFilter;

/**
 * Created by nero on 1/4/15.
 */
public class NewListFile {
    public static File[] oldHidden() {
        File[] hiddenFiles = new File(".").listFiles(new FileFilter() {
            @Override public boolean accept(File file) {
                return file.isHidden();
            }
        });
        return hiddenFiles;
    }

    public static File[] hidden() {
        File[] hiddenFiles = new File(".").listFiles(File::isHidden);
        return hiddenFiles;
    }

    public static void main(String[] args) {
        File[] files = oldHidden();
        printFiles(files);
        files = hidden();
        printFiles(files);
    }

    public static void printFiles(File[] files) {
        for (File file : files) {
            System.out.println(file.getName());
        }
        System.out.println();
    }
}
