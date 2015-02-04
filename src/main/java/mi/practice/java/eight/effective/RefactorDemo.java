package mi.practice.java.eight.effective;

public class RefactorDemo {
    public static void main(String[] args) {
        // form lambda expressions to method references
    }

    private static void anonymousToLambda() {
        // form anonymous classes to lambda expression
        Runnable r1 = new Runnable() {
            @Override public void run() {
                System.out.println("Hello");
            }
        };
        Runnable r2 = () -> System.out.println("Hello");
    }

    private void shadowVariablesInAnonymousClass() {
        // 1. this refer different in lambda and anonymous class
        // 2. shadow variables
        int a = 10;
        Runnable r1 = () -> {
            // int a =2; // can't compile
            System.out.println(a);
            System.out.println(this);
        };
        Runnable r2 = new Runnable() {
            @Override public void run() {
                int a = 2;
                System.out.println(a);
                System.out.println(this);
            }
        };
    }

    public static void doSomething(Runnable r) {
        r.run();
    }

    public static void doSomething(Task a) {
        a.execute();
    }

    private static void lambdaConflict() {
        doSomething(new Task() {
            @Override public void execute() {
                System.out.println("Danger danger!!");
            }
        });
        //doSomething(() -> System.out.println("Danger danger!!"));
        doSomething((Task) () -> System.out.println("Danger danger!!"));
    }
}

interface Task {
    public void execute();
}