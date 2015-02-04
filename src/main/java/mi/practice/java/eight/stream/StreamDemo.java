package mi.practice.java.eight.stream;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.IntSupplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
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
        menu.stream()
                .filter(Dish::isVegetarian)
                .findAny()
                .ifPresent(d -> System.out.println(d.getName()));

        List<Integer> someNumbers = Arrays.asList(1, 2, 3, 4, 5);
        Optional<Integer> firstSquareDivisibleByThree = someNumbers.stream()
                .map(x -> x * x)
                .filter(x -> x % 3 == 0)
                .findFirst();
        // findFirst and findAny is different in parallel
        // findFirst is more constraining

        /** reducing **/
        // counting
        int sum = 0;
        for (int x : numbers) {
            sum += x;
        }
        System.out.println(sum);
        sum = numbers.stream().reduce(0, (a, b) -> a + b);
        sum = numbers.stream().reduce(0, Integer::sum);
        System.out.println(sum);
        Optional<Integer> sumOptional = numbers.stream().reduce(Integer::sum);
        System.out.println(sumOptional.get());
        int product = numbers.stream().reduce(1, (a, b) -> a * b);
        System.out.println(product);
        // maximum, minimum
        Optional<Integer> max = numbers.stream().reduce(Integer::max);
        Optional<Integer> min = numbers.stream().reduce(Integer::min);
        count = menu.stream().map(a -> 1).reduce(0, Integer::sum);
        count = menu.stream().count();
        sum = numbers.parallelStream().reduce(0, Integer::sum);

        /** numeric streams **/
        int calories = menu.stream()
                .map(Dish::getCalories)
                .reduce(0, Integer::sum);
        System.out.println(calories);
        calories = menu.stream()
                .mapToInt(Dish::getCalories)
                .sum();
        IntStream intStream = menu.stream().mapToInt(Dish::getCalories);
        Stream<Integer> stream = intStream.boxed(); // converting the numeric steam to a Stream
        OptionalInt maxCalories = menu.stream()
                .mapToInt(Dish::getCalories)
                .max();
        int maxValue = maxCalories.orElse(1);
        IntStream evenNumbers = IntStream.rangeClosed(1, 100) //[1, 100]
                .filter(n -> n % 2 == 0);
        System.out.println(evenNumbers.count());
        /*IntStream.rangeClosed(1, 100)
                .filter(b -> Math.sqrt(a * a + b * b) % 1 == 0)
                .boxed()
                .map(b -> new int[] { a, b, (int) Math.sqrt(a * a + b * b) });*/
        Stream<int[]> pythagoreanTriples =
                IntStream.rangeClosed(1, 100)
                        .boxed()
                        .flatMap(a -> IntStream.rangeClosed(a, 100)
                                .filter(b -> Math.sqrt(a * a + b * b) % 1 == 0)
                                .mapToObj(b -> new int[] { a, b, (int) Math.sqrt(a * a + b * b) }));
        pythagoreanTriples.forEach(
                t -> System.out.println(t[0] + ", " + t[1] + ", " + t[2]));
        pythagoreanTriples =
                IntStream.rangeClosed(1, 100).boxed()
                        .flatMap(a -> IntStream.rangeClosed(a, 100)
                                .mapToObj(b -> new int[] { a, b, (int) Math.sqrt(a * a + b * b) })
                                .filter(t -> t[2] % 1 == 0));
        /** building stream **/
        Stream<String> stringStream = Stream.of("Java8", "Lambdas", "In", "Action");
        stringStream.map(String::toUpperCase)
                .forEach(System.out::println);
        Stream<String> emptyStream = Stream.empty();
        int[] numberArray = { 2, 3, 5, 7, 11, 13 };
        sum = Arrays.stream(numberArray).sum();
        long uniqueWords = 0;
        try (Stream<String> lines = Files.lines(Paths.get("data.txt"), Charset.defaultCharset())) {
            uniqueWords = lines.flatMap(line -> Arrays.stream(line.split(" ")))
                    .distinct()
                    .count();
        } catch (IOException e) {
            // to do with IOException
        }
        // infinite stream
        Stream.iterate(0, n -> n + 2)
                .limit(10)
                .forEach(System.out::println);
        // fibonacci
        Stream.iterate(new int[] { 1, 2 }, pair -> {
            int pairCount = pair[0] + pair[1];
            pair[0] = pair[1];
            pair[1] = pairCount;
            return pair;
        })
                .limit(20)
                .forEach(pair -> System.out.println("(" + pair[0] + "," + pair[1] + ")"));
        Stream.iterate(new int[] { 1, 2 }, pair -> new int[] { pair[1], pair[0] + pair[1] })
                .limit(20)
                .forEach(pair -> System.out.println("(" + pair[0] + "," + pair[1] + ")"));
        // generate
        Stream.generate(Math::random)
                .limit(5)
                .forEach(System.out::println);
        IntStream ones = IntStream.generate(() -> 1);
        IntStream twos = IntStream.generate(new IntSupplier() {
            @Override
            public int getAsInt() {
                return 2;
            }
        });
        IntSupplier fib = new IntSupplier() {
            private int previous = 0;
            private int current = 1;

            @Override
            public int getAsInt() {
                int oldPrevious = this.previous;
                int nextValue = this.previous + this.current;
                this.previous = this.current;
                this.current = nextValue;
                return oldPrevious;
            }
        };
        IntStream.generate(fib).limit(10).forEach(System.out::println);
        /** collecting **/
        Long howManyDishes = menu.stream().collect(Collectors.counting());
        howManyDishes = menu.stream().count();
        Comparator<Dish> dishCaloriesComparator =
                Comparator.comparingInt(Dish::getCalories);
        Optional<Dish> mostCalorieDish =
                menu.stream().collect(Collectors.maxBy(dishCaloriesComparator));
        int totalCalories = menu.stream()
                .collect(Collectors.summingInt(Dish::getCalories));
        double avgCalories = menu.stream()
                .collect(Collectors.averagingInt(Dish::getCalories));
        System.out.println(avgCalories);
        IntSummaryStatistics menuStatistics =
                menu.stream()
                        .collect(Collectors.summarizingInt(Dish::getCalories));
        System.out.println(menuStatistics);
        // joining strings
        String shortMenu = menu.stream()
                .map(Dish::getName)
                .collect(Collectors.joining());
        System.out.println(shortMenu);
        shortMenu = menu.stream()
                .map(Dish::getName)
                .collect(Collectors.joining(", "));
        System.out.println(shortMenu);
        totalCalories = menu.stream()
                .collect(Collectors.reducing(0, Dish::getCalories, (i, j) -> i + j));
        totalCalories = menu.stream()
                .collect(Collectors.reducing(0, Dish::getCalories, Integer::sum));
        mostCalorieDish = menu.stream()
                .collect(Collectors.reducing((d1, d2) -> d1.getCalories() > d2.getCalories() ? d1 : d2));
        System.out.println(mostCalorieDish);
        // collect vs reduce
        Stream<Integer> integerStream = Arrays.asList(1, 2, 3, 4, 5, 6).stream();
        numbers = integerStream.reduce(new ArrayList<>(),
                (List<Integer> list, Integer integer) -> {
                    list.add(integer);
                    return list;
                },
                (List<Integer> list1, List<Integer> list2) -> {
                    list1.addAll(list2);
                    return list1;
                });
        totalCalories = menu.stream()
                .collect(Collectors.reducing(0,
                        Dish::getCalories,
                        Integer::sum));
        totalCalories = menu.stream()
                .map(Dish::getCalories)
                .reduce(Integer::sum)
                .get();
        totalCalories = menu.stream()
                .mapToInt(Dish::getCalories)
                .sum();
        /** grouping **/
        dishesByType = menu.stream()
                .collect(Collectors.groupingBy(Dish::getType));
        System.out.println(dishesByType);
        Map<CaloricLevel, List<Dish>> dishesByCaloricLevel = menu.stream()
                .collect(Collectors.groupingBy((Dish d) -> {
                    if (d.getCalories() <= 400) {
                        return CaloricLevel.DIET;
                    } else if (d.getCalories() <= 700) {
                        return CaloricLevel.NORMAL;
                    } else {
                        return CaloricLevel.FAT;
                    }
                }));
        System.out.println(dishesByCaloricLevel);
        // multi level
        Map<Dish.Type, Map<CaloricLevel, List<Dish>>> dishesByTypeCaloricLevel =
                menu.stream()
                        .collect(Collectors.groupingBy(Dish::getType,
                                Collectors.groupingBy(
                                        (Dish d) -> {
                                            if (d.getCalories() <= 400) {
                                                return CaloricLevel.DIET;
                                            } else if (d.getCalories() <= 700) {
                                                return CaloricLevel.NORMAL;
                                            } else {
                                                return CaloricLevel.FAT;
                                            }
                                        }
                                )));
        System.out.println(dishesByTypeCaloricLevel);
        Map<Dish.Type, Long> typesCount = menu.stream().collect(
                Collectors.groupingBy(Dish::getType, Collectors.counting()));
        System.out.println(typesCount);
        Map<Dish.Type, Optional<Dish>>
                optionalMostCaloricByType = menu.stream()
                .collect(Collectors.groupingBy(Dish::getType,
                        Collectors.maxBy(Comparator.comparingInt(Dish::getCalories))));
        System.out.println(optionalMostCaloricByType);
        Map<Dish.Type, Dish> mostCaloricByType = menu.stream()
                .collect(Collectors.groupingBy(
                        Dish::getType,
                        Collectors.collectingAndThen(
                                Collectors.maxBy(Comparator.comparingInt(Dish::getCalories)),
                                Optional::get
                        )
                ));
        System.out.println(mostCaloricByType);
        Map<Dish.Type, Integer> totalCaloriesByType =
                menu.stream().collect(Collectors.groupingBy(Dish::getType,
                        Collectors.summingInt(Dish::getCalories)));
        System.out.println(totalCaloriesByType);
        Map<Dish.Type, Set<CaloricLevel>> caloricLevelsByType =
                menu.stream().collect(
                        Collectors.groupingBy(
                                Dish::getType,
                                Collectors.mapping(
                                        d -> {
                                            if (d.getCalories() <= 400) {
                                                return CaloricLevel.DIET;
                                            } else if (d.getCalories() <= 700) {
                                                return CaloricLevel.NORMAL;
                                            } else {
                                                return CaloricLevel.FAT;
                                            }
                                        },
                                        Collectors.toSet())));
        System.out.println(caloricLevelsByType);
        caloricLevelsByType =
                menu.stream().collect(
                        Collectors.groupingBy(Dish::getType, Collectors.mapping(
                                d -> {
                                    if (d.getCalories() <= 400) {
                                        return CaloricLevel.DIET;
                                    } else if (d.getCalories() <= 700) {
                                        return CaloricLevel.NORMAL;
                                    } else {
                                        return CaloricLevel.FAT;
                                    }
                                },
                                Collectors.toCollection(HashSet::new))));
        caloricLevelsByType =
                menu.stream().collect(
                        Collectors.groupingBy(Dish::getType, Collectors.mapping(
                                Dish::getCaloricLevel, Collectors.toCollection(HashSet::new))));
        System.out.println(caloricLevelsByType);
        // partitioning
        Map<Boolean, List<Dish>> partitionedMenu = menu.stream()
                .collect(Collectors.partitioningBy(Dish::isVegetarian));
        System.out.println(partitionedMenu);
        Map<Boolean, Map<Dish.Type, List<Dish>>> vegetarianDishesByType =
                menu.stream().collect(
                        Collectors.partitioningBy(Dish::isVegetarian,
                                Collectors.groupingBy(Dish::getType)));
        System.out.println(vegetarianDishesByType);
        Map<Boolean, Dish> mostCaloricPartitionedByVegetarian =
                menu.stream().collect(
                        Collectors.partitioningBy(Dish::isVegetarian,
                                Collectors.collectingAndThen(
                                        Collectors.maxBy(Comparator.comparingInt(Dish::getCalories)),
                                        Optional::get)));
        System.out.println(mostCaloricPartitionedByVegetarian);
        menu.stream().collect(Collectors.partitioningBy(Dish::isVegetarian,
                Collectors.partitioningBy((Dish d) -> d.getCalories() > 500)));
        menu.stream().collect(Collectors.partitioningBy(Dish::isVegetarian,
                Collectors.counting()));
        // use custom collector
        dishes = menu.stream()
                .collect(new ToListCollector<>());
        System.out.println(dishes);

        dishes = menu.stream()
                .collect(
                        ArrayList::new,
                        List::add,
                        List::addAll);

        System.out.println(partitionPrimes(100));

        menu.stream().filter(Dish::isVegetarian);
        menu.parallelStream().filter(Dish::isVegetarian);
    }

    public static boolean isPrime(int candidate) {
        int candidateRoot = (int) Math.sqrt(candidate);
        return IntStream.range(2, candidateRoot)
                .noneMatch(i -> candidate % i == 0);
    }

    public static Map<Boolean, List<Integer>> partitionPrimes(int n) {
        return IntStream.rangeClosed(2, n).boxed()
                .collect(
                        Collectors.partitioningBy(candidate -> isPrime(candidate)));
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

    public CaloricLevel getCaloricLevel() {
        if (calories <= 400) {
            return CaloricLevel.DIET;
        } else if (calories <= 700) {
            return CaloricLevel.NORMAL;
        } else {
            return CaloricLevel.FAT;
        }
    }
}

enum CaloricLevel {
    DIET, NORMAL, FAT
}