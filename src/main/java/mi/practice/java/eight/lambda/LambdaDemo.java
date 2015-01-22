package mi.practice.java.eight.lambda;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

/**
 * Created by nero on 1/19/15.
 */
public class LambdaDemo {
    public void printA() {
        Integer a = 2;
        Runnable r = () -> System.out.println(a);
        Thread thread = new Thread(r);
        thread.start();

        // can't modify a
        // lambda infer a is final
        // a = 3;
    }

    public static void main(String[] args) {
        // (String s)->s.length()
        // (Apple a)->a.getWeigt()<150
        /*(int i, int y) -> {
            System.out.println("Result: ");
            System.out.println(x + y);
        }*/
        //() -> 42
        List<String> strings = new ArrayList<>();
        strings.sort((String s, String s2) -> s2.compareTo(s));

        Comparator<String> revertSort = (String s1, String s2) -> s2.compareTo(s1);
        Runnable runnable = () -> System.out.println("Run in lambda interface");
        Thread thread = new Thread(runnable);
        thread.start();
        System.out.println("run in the main thread");

        /*Object o = () -> {
            System.out.println("Tricky example");
        };*/

        Runnable runnable1 = () -> {
            System.out.println("Tricky example");
        };

        int a = 2;
        Runnable r = () -> System.out.println(a);
        thread = new Thread(r);
        thread.start();

        System.out.println("Done");
    }

    public static List<String> filterEmptyString(List<String> strings) {
        return filter(strings, (String s) -> !s.isEmpty());
    }

    public static <T> List<T> filter(List<T> list, Predicate<T> p) {
        List<T> result = new ArrayList<>();
        for (T s : list) {
            if (p.test(s)) {
                result.add(s);
            }
        }
        return result;
    }
}
