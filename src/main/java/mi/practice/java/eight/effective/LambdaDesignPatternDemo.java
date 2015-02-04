package mi.practice.java.eight.effective;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

/**
 * Created by nero on 2/3/15.
 */
public class LambdaDesignPatternDemo {
    public static void main(String[] args) {
        strategy();
        observer();
        chainOfResponsibility();
        // makeError();
        // divideByZeroDemo();
        List<Integer> numbers = Arrays.asList(1, 2, 3);
        List<Integer> result =
                numbers.stream()
                        .peek(x -> System.out.println("from stream: " + x))
                        .map(x -> x + 17)
                        .peek(x -> System.out.println("after map: " + x))
                        .filter(x -> x % 2 == 0)
                        .peek(x -> System.out.println("after filter: " + x))
                        .limit(3)
                        .peek(x -> System.out.println("after limit: " + x))
                        .collect(Collectors.toList());

    }

    private static void divideByZeroDemo() {
        List<Integer> numbers = Arrays.asList(1, 2, 3);
        numbers.stream().map(LambdaDesignPatternDemo::divideByZero)
                .forEach(System.out::println);
    }

    public static int divideByZero(int n) {
        return n / 0;
    }

    private static void makeError() {
        List<Point> points = Arrays.asList(new Point(12, 2), null);
        points.stream().map(p -> p.getX()).forEach(System.out::println);
    }

    private static void chainOfResponsibility() {
        // chain of responsibility
        ProcessingObject<String> p1 = new HeaderTextProcessing();
        ProcessingObject<String> p2 = new SpellCheckerProcessing();
        p1.setSuccessor(p2);
        String result = p1.handle("Aren't labdas really sexy?!!");
        System.out.println(result);
        // with lambda
        UnaryOperator<String> headerProcessing =
                (text) -> "From Raoul, Mario and Alan: " + text;
        UnaryOperator<String> spellCheckerProcessing =
                (text) -> text.replaceAll("labda", "lambda");
        Function<String, String> pipeline =
                headerProcessing.andThen(spellCheckerProcessing);
        result = pipeline.apply("Aren't labdas really sexy?!!");
        System.out.println(result);
    }

    private static void observer() {
        // observer
        Feed f = new Feed();
        f.registerObserver(new NYTimes());
        f.registerObserver(new Guardian());
        f.registerObserver(new LeMonde());
        f.notifyObservers("The queen said her favourite book is Java 8 in Action!");
        // with lambda
        f = new Feed();
        f.registerObserver(generateObserver("money", "Breaking news in NY!"));
        f.registerObserver(generateObserver("queen", "Yet another news in London..."));
        f.notifyObservers("The queen said her favourite book is Java 8 in Action!");
    }

    private static Observer generateObserver(String keyword, String out) {
        return (tweet) -> {
            if (tweet != null && tweet.contains(keyword)) {
                System.out.println(out + tweet);
            }
        };
    }

    private static void strategy() {
        // strategy
        Validator numericValidator = new Validator(new IsNumeric());
        boolean b1 = numericValidator.validate("aaaa");
        System.out.println(b1);
        Validator lowerCaseValidator = new Validator(new IsAllLowerCase());
        boolean b2 = lowerCaseValidator.validate("bbbb");
        System.out.println(b2);
        // with lambda
        numericValidator = new Validator((String s) -> s.matches("\\d+"));
        b1 = numericValidator.validate("aaaa");
        System.out.println(b1);
        lowerCaseValidator = new Validator(s -> s.matches("[a-z]+"));
        b2 = lowerCaseValidator.validate("bbbb");
        System.out.println(b2);
    }
}

@FunctionalInterface interface ValidationStrategy {
    abstract boolean execute(String s);
}

class IsAllLowerCase implements ValidationStrategy {
    @Override public boolean execute(String s) {
        return s.matches("[a-z]+");
    }
}

class IsNumeric implements ValidationStrategy {

    @Override public boolean execute(String s) {
        return s.matches("\\d+");
    }
}

class Validator {
    private final ValidationStrategy strategy;

    public Validator(ValidationStrategy strategy) {
        this.strategy = strategy;
    }

    public boolean validate(String s) {
        return this.strategy.execute(s);
    }
}

interface Observer {
    void notify(String tweet);
}

class NYTimes implements Observer {

    @Override public void notify(String tweet) {
        if (tweet != null && tweet.contains("money")) {
            System.out.println("Breaking news in NY!" + tweet);
        }
    }
}

class Guardian implements Observer {

    @Override public void notify(String tweet) {
        if (tweet != null && tweet.contains("queen")) {
            System.out.println("Yet another news in London... " + tweet);
        }
    }
}

class LeMonde implements Observer {
    @Override public void notify(String tweet) {
        if (tweet != null && tweet.contains("wine")) {
            System.out.println("Today cheese, wine and news!" + tweet);
        }
    }
}

interface Subject {
    void registerObserver(Observer o);

    void notifyObservers(String tweet);
}

class Feed implements Subject {
    private final List<Observer> observers = new ArrayList<>();

    @Override public void registerObserver(Observer o) {
        this.observers.add(o);
    }

    @Override public void notifyObservers(String tweet) {
        observers.forEach(o -> o.notify(tweet));
    }
}

abstract class ProcessingObject<T> {
    protected ProcessingObject<T> successor;

    public void setSuccessor(ProcessingObject<T> successor) {
        this.successor = successor;
    }

    public T handle(T input) {
        T r = handleWork(input);
        if (successor != null) {
            return successor.handle(r);
        }
        return r;
    }

    abstract protected T handleWork(T input);
}

class HeaderTextProcessing extends ProcessingObject<String> {
    @Override protected String handleWork(String input) {
        return "From Raoul, Mario and Alan: " + input;
    }
}

class SpellCheckerProcessing extends ProcessingObject<String> {
    @Override protected String handleWork(String input) {
        return input.replaceAll("labda", "lambda");
    }
}

class Point {
    private final int x;
    private final int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Point moveRightBy(int x) {
        return new Point(this.x + x, this.y);
    }
}