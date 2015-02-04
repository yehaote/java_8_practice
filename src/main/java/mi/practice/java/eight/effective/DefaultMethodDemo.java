package mi.practice.java.eight.effective;

/**
 * Created by nero on 2/3/15.
 */
public class DefaultMethodDemo {
    public static void main(String[] args) {
        Table table = new Table();
        // can call the default method in interface
        System.out.println(table.isEmpty());
    }
}

@FunctionalInterface interface Sized {
    int size();

    default boolean isEmpty() {
        return size() == 0;
    }
}

/**
 * just need to implements size() method
 */
class Table implements Sized {
    @Override
    public int size() {
        return 10;
    }
}

interface A {
    default void hello() {
        System.out.println("Hello from A");
    }
}

interface B extends A {
    @Override
    default void hello() {
        System.out.println("Hello from B");
    }
}

class C implements B, A {
    public static void main(String[] args) {
        new C().hello();
    }
}

class D implements A {
    @Override
    public void hello() {
        System.out.println("Hello from D");
    }
}

class E extends D implements B, A {
    public static void main(String[] args) {
        System.out.print("E say hello, ");
        new E().hello();
    }
}