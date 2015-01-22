package mi.practice.java.eight.lambda;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * method reference
 * syntactic sugar
 * Created by nero on 1/20/15.
 */
public class MethodReference {
    public static Random random = new Random();

    public static void main(String[] args) {
        List<Apple> apples = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            apples.add(new Apple(random.nextInt(10)));
        }
        System.out.println(apples);
        // before
        apples.sort((a, b) -> {
            int result = a.getWeight().compareTo(b.getWeight());
            return result;
        });
        System.out.println(apples);
        // after
        apples.sort(Comparator.comparing(Apple::getWeight));
        System.out.println(apples);
        apples.sort(Comparator.comparing((Apple a) -> (a.getWeight())));
        System.out.println(apples);

        Comparator<String> stringVerbose = (String a, String b) -> a.compareTo(b);
        Comparator<String> stringMethodReference = String::compareTo;

        Check check = () -> apples.isEmpty();
        check = apples::isEmpty;

        List<String> strings = Arrays.asList("a", "b", "A", "B");
        strings.sort((s1, s2) -> s1.compareToIgnoreCase(s2));
        strings.sort(String::compareToIgnoreCase);

        Function<String, Integer> stringToInteger = Integer::parseInt;
        BiPredicate<List<String>, String> contains = List::contains;
    }

    public static void constructorReference() {
        Supplier<Apple> c1 = Apple::new;
        Apple a1 = c1.get();
        Supplier<Apple> c2 = () -> new Apple();
        Apple a2 = c1.get();

        Function<Integer, Apple> c3 = Apple::new;
        Apple a3 = c3.apply(110);
        Function<Integer, Apple> c4 = (Integer weight) -> new Apple(weight);
        Apple a4 = c4.apply(120);

        List<Integer> weights = Arrays.asList(7, 3, 4, 10);
        List<Apple> apples = map(weights, Apple::new);

        BiFunction<String, Integer, Apple> c5 = Apple::new;
        Apple a5 = c5.apply("green", 130);
        BiFunction<String, Integer, Apple> c6 = (color, weight) -> new Apple(color, weight);
        Apple a6 = c6.apply("red", 140);

        Fruit f1 = giveMeFruit("apple", 150);
        Fruit f2 = giveMeFruit("orange", 160);
    }

    public static List<Apple> map(List<Integer> weights, Function<Integer, Apple> f) {
        List<Apple> result = new ArrayList<>();
        for (Integer weight : weights) {
            result.add(f.apply(weight));
        }
        return result;
    }

    static Map<String, Function<Integer, Fruit>> map = new HashMap<>();

    static {
        map.put("apple", Apple::new);
        map.put("orange", Orange::new);
    }

    public static Fruit giveMeFruit(String fruit, Integer weight) {
        return map.get(fruit).apply(weight);
    }

}

@FunctionalInterface interface Check {
    boolean check();
}

class Apple extends Fruit {
    private int weight;
    private String color;
    private int country;

    public Apple() {
        this.weight = 0;
    }

    public Apple(int weight) {
        this.weight = weight;
    }

    public Apple(String color, int weight) {
        this.weight = weight;
        this.color = color;
    }

    public Integer getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return "Apple:" + weight;
    }

    public int getCountry() {
        return country;
    }

    public void setCountry(int country) {
        this.country = country;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}

class Orange extends Fruit {
    public Orange(int weight) {
        this.weight = weight;
    }

    private int weight;
}

class Fruit {

}
