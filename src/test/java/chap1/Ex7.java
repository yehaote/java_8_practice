package chap1;

public class Ex7 {
    public static void main(String[] args) {
        Thread thread = new Thread(
                andThen(() -> System.out.println("This is first"),
                        () -> System.out.println("This is then")));
        thread.start();
    }

    public static Runnable andThen(Runnable first, Runnable then) {
        return () -> {
            first.run();
            then.run();
        };
    }
}
