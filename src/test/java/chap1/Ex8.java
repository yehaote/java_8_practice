package chap1;

import java.util.ArrayList;
import java.util.List;

public class Ex8 {
    public static void main(String[] args) {
        String[] names = { "Peter", "Paul", "Mary" };
        List<Runnable> runners = new ArrayList<>();
        for(String name:names){
            runners.add(() -> System.out.println(Thread.currentThread().getName() + "," + name));
        }
        /*for (int i = 0; i < names.length; i++) {
            String name = names[i];
            runners.add(() -> System.out.println(Thread.currentThread().getName() + "," + name));
        }*/

        for (Runnable runner : runners) {
            Thread thread = new Thread(runner);
            thread.start();
        }

    }
}
