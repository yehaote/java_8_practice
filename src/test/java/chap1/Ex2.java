package chap1;

import java.io.File;

public class Ex2 {
    public static void main(String[] args) {
        File file = new File("C:/");
        File[] result = file.listFiles((File f) -> f.isDirectory());
        printFiles(result);
        result = file.listFiles(File::isDirectory);
        printFiles(result);
    }

    private static void printFiles(File[] files) {
        for (File file : files) {
            System.out.println(file.getName());
        }
    }
}
