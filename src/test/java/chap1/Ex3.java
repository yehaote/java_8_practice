package chap1;

import java.io.File;

public class Ex3 {
    public static void main(String[] args) {
        File file = new File("C:\\Users\\ner6\\Downloads\\");
        String[] result = filterFileByExtends(file, ".exe");
        printFileNames(result);
        System.out.println();
        result = filterFileByExtends(file, ".zip");
        printFileNames(result);
    }

    private static String[] filterFileByExtends(File dir, String suffix) {
        return dir.list((File f, String name) -> name.endsWith(suffix));
    }

    private static void printFileNames(String[] fileNames) {
        for (String fileName : fileNames) {
            System.out.println(fileName);
        }
    }
}
