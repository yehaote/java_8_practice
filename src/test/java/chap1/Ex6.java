package chap1;

import java.util.concurrent.Callable;

@FunctionalInterface
interface RunnableEx {
    public abstract void run() throws Exception;
}

public class Ex6 {
    public static void main(String[] args) {
        new Thread(unchecked(() -> {
            System.out.println("Zzz");
            Thread.sleep(1000);
        })).start();

        new Thread(uncheckedWithCall(() -> {
            System.out.println("Zzz");
            Thread.sleep(1000);
            return null;
        })).start();

    }

    private static Runnable unchecked(RunnableEx runnableEx) {
        return () -> {
            try {
                runnableEx.run();
            } catch (Exception e) {
                System.err.println(e);
            }
        };
    }

    private static Runnable uncheckedWithCall(Callable<Void> callable) {
        return () -> {
            try {
                callable.call();
            } catch (Exception e) {
                System.err.println(e);
            }
        };
    }
}
