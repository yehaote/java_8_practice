package mi.practice.java.eight.stream;

import java.util.concurrent.Callable;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class ParallelStreamDemo {

    public static long sequentialSum(long n) {
        return Stream.iterate(1L, i -> i + 1)
                .limit(n)
                .reduce(0L, Long::sum);
    }

    public static long iterativeSum(long n) {
        // optimize by compile?
        long result = 0L;
        for (long i = 1L; i <= n; i++) {
            result += i;
        }
        return result;
    }

    public static long parallelSum(long n) {
        // ? slow than sequential
        return Stream.iterate(1L, i -> i + 1)
                .limit(n)
                .parallel()
                .reduce(0L, (a, b) -> {
                    //System.out.println(Thread.currentThread().getName());
                    return a + b;
                });
    }

    public static long measureSumPerformance(Function<Long, Long> adder, long n) {
        long fastest = Long.MAX_VALUE;
        for (int i = 0; i < 10; i++) {
            long start = System.nanoTime();
            long sum = adder.apply(n);
            long duration = (System.nanoTime() - start) / 1_000_000;
            System.out.println("Result:" + sum);
            if (duration < fastest) {
                fastest = duration;
            }
        }
        return fastest;
    }

    public static long rangedSum(long n) {
        return LongStream.rangeClosed(1, n)
                .reduce(0L, Long::sum);
    }

    public static long parallelRangedSum(long n) {
        return LongStream.rangeClosed(1, n)
                .parallel()
                .reduce(0L, Long::sum);
    }

    public static long sideEffectSum(long n) {
        Accumulator accumulator = new Accumulator();
        LongStream.rangeClosed(1, n).forEach(accumulator::add);
        return accumulator.total;
    }

    public static long sideEffectParallelSum(long n) {
        Accumulator accumulator = new Accumulator();
        LongStream.rangeClosed(1, n).parallel().forEach(accumulator::add);
        return accumulator.total;
    }

    public static long sideEffectParallelSumWithAtomic(long n) {
        AtomicLong accumulator = new AtomicLong();
        LongStream.rangeClosed(1, n).parallel().forEach(accumulator::getAndAdd);
        return accumulator.get();
    }

    public static long forkJoinSum(long n){
        long[] numbers = LongStream.rangeClosed(1,n).toArray();
        ForkJoinTask<Long> task = new ForkJoinSumCalculator(numbers);
        return new ForkJoinPool().invoke(task);
    }

    public static void main(String[] args) {
        /*System.out.println(Runtime.getRuntime().availableProcessors());
        System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "12");
        long count = 1000L;
        cost(() -> iterativeSum(count));
        cost(() -> sequentialSum(count));
        cost(() -> parallelSum(count));*/

        long count = 10_000_000L;
        //printMeasure("Sequential", ParallelStreamDemo::sequentialSum, count);
        //printMeasure("Iterative", ParallelStreamDemo::iterativeSum, count);
        //printMeasure("Parallel", ParallelStreamDemo::parallelSum, count);
        printMeasure("LongStream sequential", ParallelStreamDemo::rangedSum, count);
        printMeasure("LongStream parallel", ParallelStreamDemo::parallelRangedSum, count);
        printMeasure("Side effect sum sequential", ParallelStreamDemo::sideEffectSum, count);
        printMeasure("Side effect sum parallel", ParallelStreamDemo::sideEffectParallelSum, count);
        printMeasure("Side effect sum parallel with atomic", ParallelStreamDemo::sideEffectParallelSumWithAtomic, count);
        printMeasure("ForkJoin", ParallelStreamDemo::forkJoinSum, count);
    }

    public static void printMeasure(String name, Function<Long, Long> adder,
            long count) {
        System.out.println(name + " sum done in:"
                + measureSumPerformance(adder, count)
                + " milliSeconds");
    }

    public static long cost(Callable<Long> callable) {
        long result = -1L;
        long beginTime = System.currentTimeMillis();
        try {
            result = callable.call();
            System.out.println(callable + " cost " + (System.currentTimeMillis() - beginTime) + " millis, result is " + result);
        } catch (Exception e) {
            System.err.print(e);
        }
        return result;
    }
}

class Accumulator {
    public long total = 0;

    public void add(long value) {
        total += value;
    }
}
