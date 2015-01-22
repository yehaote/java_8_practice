package mi.practice.java.eight.lambda;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.DoubleFunction;
import java.util.function.Function;
import java.util.function.Predicate;

public class UsefulMethod {
    public static void main(String[] args) {
        /** Comparator **/
        List<Apple> inventory = new ArrayList<>();
        // compose lambda
        Comparator<Apple> comparator = Comparator.comparing(Apple::getWeight);
        inventory.sort(comparator);
        // reversed
        inventory.sort(Comparator.comparing(Apple::getWeight).reversed());
        // chaining
        inventory.sort(Comparator.comparing(Apple::getWeight)
                .reversed()
                .thenComparing(Apple::getCountry)); // work when weight equals

        /** Predicate **/
        Predicate<Apple> redApple = apple -> ("red".equals(apple.getColor()));
        Predicate<Apple> notRedApple = redApple.negate();
        Predicate<Apple> redAndHeavyApple = redApple.and(apple -> apple.getWeight() > 150);
        Predicate<Apple> redAndHeavyOrGreen = redApple
                .and(apple -> apple.getWeight() > 150)
                .or(apple -> "green".equals(apple.getColor()));
        // a.or(b).and(c) == (a || b) && c

        /** Function **/
        Function<Integer, Integer> f = x -> x + 1;
        Function<Integer, Integer> g = x -> x * 2;
        Function<Integer, Integer> h = f.andThen(g); // h(x) = g(f(x))
        int result = h.apply(1);
        System.out.println(result); // 4

        Function<Integer, Integer> i = f.compose(g); // i(x) = f(g(x))
        result = i.apply(1);
        System.out.println(result); // 3

        Function<String, String> addHeader = Letter::addHeader;
        Function<String, String> transformationPipleline = addHeader
                .andThen(Letter::checkSpelling)
                .andThen(Letter::addFooter);
        transformationPipleline = addHeader.andThen(Letter::addFooter);
    }

    public static double intergrate(DoubleFunction<Double> f, double a, double b) {
        return (f.apply(a) + f.apply(b)) * (b - a) / 2.0;
    }
}

class Letter {
    public static String addHeader(String text) {
        return "From Raoul, Mario and Alan:" + text;
    }

    public static String addFooter(String text) {
        return text + "Kind regards";
    }

    public static String checkSpelling(String text) {
        return text.replaceAll("albda", "lambda");
    }
}

