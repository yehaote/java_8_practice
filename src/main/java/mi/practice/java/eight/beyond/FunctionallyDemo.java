package mi.practice.java.eight.beyond;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.DoubleUnaryOperator;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

public class FunctionallyDemo {
    public static void main(String[] args) {
        List<Integer> integers = Arrays.asList(1, 4, 9);
        System.out.println(subsets(integers));
    }

    public static <T> List<List<T>> subsets(List<T> list) {
        if (list.isEmpty()) {
            List<List<T>> ans = new ArrayList<>();
            ans.add(Collections.emptyList());
            return ans;
        }
        T first = list.get(0);
        List<T> rest = list.subList(1, list.size());

        List<List<T>> subans = subsets(rest);
        List<List<T>> subans2 = insertAll(first, subans);
        return concat(subans, subans2);
    }

    public static <T> List<List<T>> insertAll(T first, List<List<T>> lists) {
        List<List<T>> result = new ArrayList<>();
        for (List<T> list : lists) {
            List<T> copyList = new ArrayList<>();
            copyList.add(first);
            copyList.addAll(list);
            result.add(copyList);
        }
        return result;
    }

    public static <T> List<List<T>> concat(List<List<T>> a, List<List<T>> b) {
        List<List<T>> r = new ArrayList<>();
        r.addAll(a);
        r.addAll(b);
        return r;
    }

    public static int factorialIterative(int n) {
        int r = 1;
        for (int i = 1; i < n; i++) {
            r *= i;
        }
        return r;
    }

    public static long factorialRecursive(long n) {
        return n == 1 ? 1 : n * factorialRecursive(n - 1);
    }

    public static long factorialStreams(long n) {
        return LongStream.rangeClosed(1, n)
                .reduce(1, (a, b) -> a * b);
    }

    static long factorialTailRecursive(long n) {
        return factorialHelper(1, n);
    }

    static long factorialHelper(long acc, long n) {
        return n == 1 ? acc : factorialHelper(acc * n, n - 1);
    }

    static double converter(double x, double f, double b) {
        return x * f + b;
    }

    static DoubleUnaryOperator curriedConverter(double f, double b) {
        return (double x) -> x * f + b;
    }

    static void converterDemo() {
        DoubleUnaryOperator convertCtoF = curriedConverter(9.0 / 5, 32);
        DoubleUnaryOperator convertUSDtoGBP = curriedConverter(0.6, 0);
        DoubleUnaryOperator convertKmtoMi = curriedConverter(0.6214, 0);

        double gbp = convertUSDtoGBP.applyAsDouble(1000);
    }

    static TrainJourney link(TrainJourney a, TrainJourney b) {
        if (a == null)
            return b;
        TrainJourney t = a;
        while (t.onward != null) {
            t = t.onward;
        }
        t.onward = b;
        return a;
    }

    static TrainJourney append(TrainJourney a, TrainJourney b) {
        return a == null ? b : new TrainJourney(a.price, append(a.onward, b));
    }
}

class TrainJourney {
    public int price;
    public TrainJourney onward;

    public TrainJourney(int price, TrainJourney onward) {
        this.price = price;
        this.onward = onward;
    }
}

class Tree {
    public String key;
    public int val;
    public Tree left, right;

    public Tree(String key, int val, Tree left, Tree right) {
        this.key = key;
        this.val = val;
        this.left = left;
        this.right = right;
    }
}

class TreeProcessor {
    public static int lookup(String k, int defaultVal, Tree t) {
        if (t == null)
            return defaultVal;
        if (k.equals(t.key))
            return t.val;
        return lookup(k, defaultVal,
                k.compareTo(t.key) < 0 ? t.left : t.right);
    }

    public static Tree update(String k, int newVal, Tree t) {
        if (t == null) {
            t = new Tree(k, newVal, null, null);
        } else if (k.equals(t.key))
            t.val = newVal;
        else if (k.compareTo(t.key) < 0)
            t.left = update(k, newVal, t.left);
        else
            t.right = update(k, newVal, t.right);
        return t;
    }

    public static Tree functionalUpdate(String k, int newVal, Tree t) {
        return (t == null) ?
                new Tree(k, newVal, null, null) :
                k.equals(t.key) ?
                        new Tree(k, newVal, t.left, t.right) :
                        k.compareTo(t.key) < 0 ?
                                new Tree(t.key, t.val, functionalUpdate(k, newVal, t.left), t.right) :
                                new Tree(t.key, t.val, t.left, functionalUpdate(k, newVal, t.right));
    }

    static IntStream numbers() {
        return IntStream.iterate(2, n -> n + 1);
    }

    static int head(IntStream numbers) {
        return numbers.findFirst().getAsInt();
    }

    static IntStream tail(IntStream numbers) {
        return numbers.skip(1);
    }

    static IntStream primes(IntStream numbers) {
        int head = head(numbers);
        return IntStream.concat(
                IntStream.of(head),
                primes(tail(numbers).filter(n -> n % head != 0))
        );
    }
}