package mi.practice.java.eight.stream;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamDemo {
    public static void main(String[] args) {
        List<Dish> menu = new ArrayList<>();
        // before
        List<Dish> lowCaloricDishes = new ArrayList<>();
        for (Dish dish : menu) {
            if (dish.getCalories() < 400) {
                lowCaloricDishes.add(dish);
            }
        }
        Collections.sort(lowCaloricDishes, new Comparator<Dish>() {
            @Override public int compare(Dish d1, Dish d2) {
                return Integer.compare(d1.getCalories(), d2.getCalories());
            }
        });
        List<String> lowCaloricDishesName = new ArrayList<>();
        for (Dish dish : lowCaloricDishes) {
            lowCaloricDishesName.add(dish.getName());
        }
        // java 8
        lowCaloricDishesName = menu.stream()
                .filter(d -> d.getCalories() < 400)
                .sorted(Comparator.comparing(Dish::getCalories))
                .map(Dish::getName)
                .collect(Collectors.toList());
        // parallel
        lowCaloricDishesName = menu.parallelStream() // execute in parallel
                .filter(d -> d.getCalories() < 400)
                .sorted(Comparator.comparing(Dish::getCalories))
                .map(Dish::getName)
                .collect(Collectors.toList());
        Map<Dish.Type, List<Dish>> dishesByType = menu
                .stream()
                .collect(Collectors.groupingBy(Dish::getType));

        menu = Arrays.asList(
                new Dish("pork", false, 800, Dish.Type.MEAT),
                new Dish("beef", false, 700, Dish.Type.MEAT),
                new Dish("chicken", false, 400, Dish.Type.MEAT),
                new Dish("french fries", true, 530, Dish.Type.OTHER),
                new Dish("rice", true, 350, Dish.Type.OTHER),
                new Dish("season fruit", true, 120, Dish.Type.OTHER),
                new Dish("pizza", true, 550, Dish.Type.OTHER),
                new Dish("prawns", false, 300, Dish.Type.FISH),
                new Dish("salmon", false, 450, Dish.Type.FISH));

        List<String> threeHighCaloricDishNames =
                menu.stream()
                        .filter(d -> d.getCalories() > 300)
                        .map(Dish::getName)
                        .limit(3)
                        .collect(Collectors.toList());
        System.out.println(threeHighCaloricDishNames);

        List<String> titles = Arrays.asList("Java8", "in", "Action");
        Stream<String> s = titles.stream();
        s.forEach(System.out::println);
        // s.forEach(System.out::println); // consume only once

        // external iteration
        List<String> names = new ArrayList<>();
        for (Dish d : menu) {
            names.add(d.getName());
        }
        // external with iterator
        Iterator<Dish> iterator = menu.iterator();
        while (iterator.hasNext()) {
            Dish d = iterator.next();
            names.add(d.getName());
        }
        // internal iteration
        names = menu.stream()
                .map(Dish::getName)
                .collect(Collectors.toList());
        /** intermediate and terminal **/
        // short-circuiting
        // loop fusion
        names = menu.stream()
                .filter(d -> {
                    System.out.println("filtering " + d.getName());
                    return d.getCalories() > 300;
                })
                .map(d -> {
                    System.out.println("mapping " + d.getName());
                    return d.getName();
                })
                .limit(3)
                .collect(Collectors.toList());
        System.out.println(names);

        /** terminal operation**/
        menu.stream().forEach(System.out::println);
        long count = menu.stream()
                .filter(d -> d.getCalories() > 600)
                .distinct()
                .limit(3)
                .count();
        System.out.println(count);

        List<Dish> vegetarianDishes = new ArrayList<>();
        for (Dish d : menu) {
            if (d.isVegetarian()) {
                vegetarianDishes.add(d);
            }
        }
        vegetarianDishes = menu.stream()
                .filter(Dish::isVegetarian)
                .collect(Collectors.toList());

        /** filtering and slicing **/
        List<Integer> numbers = Arrays.asList(1, 2, 1, 3, 3, 2, 4);
        numbers.stream()
                .filter((i) -> i % 2 == 0)
                .distinct()
                .forEach(System.out::println);

        List<Dish> dishes = menu.stream()
                .filter(d -> d.getCalories() > 300)
                .limit(3)
                .collect(Collectors.toList());

        dishes = menu.stream()
                .filter(d -> d.getCalories() > 300)
                .skip(2)
                .collect(Collectors.toList());

        dishes = menu.stream()
                .filter(d -> d.getType().equals(Dish.Type.MEAT))
                .limit(2)
                .collect(Collectors.toList());

        /** mapping **/
        List<String> dishNames = menu.stream()
                .map(Dish::getName)
                .collect(Collectors.toList());

        List<String> words = Arrays.asList("Java8", "Lambdas", "In", "Action");
        List<Integer> wordLengths = words.stream()
                .map(String::length)
                .collect(Collectors.toList());
        System.out.println(wordLengths);

        List<Integer> dishNameLengths = menu.stream()
                .map(Dish::getName)
                .map(String::length)
                .collect(Collectors.toList());
        System.out.println(dishNameLengths);

        words = Arrays.asList("Hello", "World");
        /* The result of map is List<String[]>
        List<String> uniqueCharacters = words.stream()
                .map(w->w.split(""))
                .distinct()
                .collect(Collectors.toList());
        System.out.println(uniqueCharacters);*/

        String[] arrayOfWords = { "Hello", "World" };
        Stream<String> streamOfWords = Arrays.stream(arrayOfWords);

        List<String> uniqueCharacters = words.stream()
                .map(w -> w.split(""))
                .flatMap(Arrays::stream)
                .distinct()
                .collect(Collectors.toList());
        System.out.println(uniqueCharacters);

        /**
         * The flatMap() operation has the effect of applying a one-to-many
         * transformation to the elements of the stream, and then flattening the
         * resulting elements into a new stream.
         */

        numbers = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> squareNumbers = numbers.stream()
                .map(num -> num * num)
                .collect(Collectors.toList());
        System.out.println(squareNumbers);

        List<Integer> numberList1 = Arrays.asList(1, 2, 3);
        List<Integer> numberList2 = Arrays.asList(3, 4);
        List<Integer[]> pairs = numberList1.stream()
                .flatMap(num -> numberList2.stream()
                        .map(num2 -> new Integer[] { num, num2 }))
                .collect(Collectors.toList());
        pairs.forEach(pair -> System.out.println("[" + pair[0] + ", " + pair[1] + "]"));
        System.out.println();

        List<Integer[]> sumThree = numberList1.stream()
                .flatMap(num -> numberList2.stream()
                        .filter(num2 -> (num + num2) % 3 == 0)
                        .map(num2 -> new Integer[] { num, num2 }))
                .collect(Collectors.toList());
        sumThree.forEach(pair -> System.out.println("[" + pair[0] + ", " + pair[1] + "]"));

        /** finding and matching **/
        // Is there an element match predicate?
        if (menu.stream().anyMatch(Dish::isVegetarian)) {
            System.out.println("The menu is (somewhat) vegetarian friendly!!");
        }
        // Is all match?
        boolean isHealthy = menu.stream()
                .allMatch(d -> d.getCalories() < 1000);
        // no element match
        isHealthy = menu.stream()
                .noneMatch(d -> d.getCalories() >= 1000);
        Optional<Dish> dish = menu.stream()
                .filter(Dish::isVegetarian)
                .findAny();
    }
}

class Dish {
    private final String name;
    private final boolean vegetarian;
    private final int calories;
    private final Type type;

    public Dish(String name, boolean vegetarian, int calories, Type type) {
        this.name = name;
        this.vegetarian = vegetarian;
        this.calories = calories;
        this.type = type;
    }

    public boolean isVegetarian() {
        return vegetarian;
    }

    public int getCalories() {
        return calories;
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    @Override
    public String toString() {
        return name;
    }

    enum Type {
        MEAT, FISH, OTHER
    }
}
