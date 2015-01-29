package mi.practice.java.eight.stream;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 * Created by nero on 1/26/15.
 */
public class ToListCollectorDemo {
}

class ToListWithSetCollector<T> implements Collector<T, List<T>, Set<T>> {
    @Override
    public Supplier<List<T>> supplier() {
        // T get();
        //return () -> new ArrayList<>();
        return ArrayList::new;
    }

    @Override
    public BiConsumer<List<T>, T> accumulator() {
        // void accept(T t, U u);
        //return (list, value) -> list.add(value);
        return List::add;
    }

    @Override
    public BinaryOperator<List<T>> combiner() {
        // T apply(T t, T u);
        return (list1, list2) -> {
            list1.addAll(list2);
            return list1;
        };
    }

    @Override
    public Function<List<T>, Set<T>> finisher() {
        // R apply(T t);
        // Set<T> apply(List<T> t)
        // return (list) -> new HashSet<>(list);
        return HashSet::new;
    }

    /**
     * Returns a {@code Set} of {@code Collector.Characteristics} indicating
     * the characteristics of this Collector.  This set should be immutable.
     *
     * @return an immutable set of collector characteristics
     */
    @Override
    public Set<Characteristics> characteristics() {
        return null;
    }
}

class ToListCollector<T> implements Collector<T, List<T>, List<T>> {

    @Override
    public Supplier<List<T>> supplier() {
        return () -> new ArrayList<>();
    }

    /**
     * Add data to accumulator
     *
     * @return
     */
    @Override
    public BiConsumer<List<T>, T> accumulator() {
        // return (list, value) -> list.add(value);
        return List::add;
    }

    @Override
    public BinaryOperator<List<T>> combiner() {
        return (list1, list2) -> {
            list1.addAll(list2);
            return list1;
        };
    }

    /**
     * convert accumulator to result
     *
     * @return
     */
    @Override
    public Function<List<T>, List<T>> finisher() {
        //return (list) -> list;
        return Function.identity();
    }

    @Override
    public Set<Characteristics> characteristics() {
        return Collections.unmodifiableSet(
                EnumSet.of(Characteristics.IDENTITY_FINISH, Characteristics.CONCURRENT)
        );
    }
}