package mi.practice.java.eight.effective;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class FutureDemo {
    private static final List<Shop> SHOPS = Arrays.asList(new Shop("BestPrice"),
            new Shop("LetsSaveBig"),
            new Shop("MyFavoriteShop"),
            new Shop("BuyItAll"),
            new Shop("ShopEasy"));
    private static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(Math.min(SHOPS.size(), 100),
            (runnable) -> {
                Thread thread = new Thread(runnable);
                thread.setDaemon(true);
                return thread;
            });

    public static void main(String[] args) {
        /*cost(FutureDemo::findPrices);
        cost(FutureDemo::findPricesParallel);
        cost(FutureDemo::findPricesWithFuture);*/
        cost(FutureDemo::findPricesWithDiscount);
        cost(FutureDemo::findPriceWithDiscountFuture);
    }

    public static List<String> findPriceWithDiscountFuture(String product) {
        List<CompletableFuture<String>> priceFutures =
                SHOPS.stream()
                        .map(shop -> CompletableFuture.supplyAsync(
                                () -> shop.getPrice(product),
                                EXECUTOR))
                        .map(future -> future.thenApply(Quote::parse))
                        .map(future -> future.thenCompose(
                                quote -> CompletableFuture.supplyAsync(
                                        () -> Discount.applyDiscount(quote), EXECUTOR)))
                        .collect(toList());
        return priceFutures.stream()
                .map(CompletableFuture::join)
                .collect(toList());
    }

    public static List<String> findPricesWithDiscount(String product) {
        return SHOPS.stream()
                .map(shop -> shop.getPrice(product))
                .map(Quote::parse)
                .map(Discount::applyDiscount)
                .collect(toList());
    }

    public static void cost(Function<String, List<String>> findPrice) {
        long start = System.nanoTime();
        System.out.println(findPrice.apply("myPhone27S"));
        long duration = (System.nanoTime() - start) / 1_000_000;
        System.out.println("Done in " + duration + " msecs");
    }

    public static List<String> findPricesWithFuture(String product) {
        List<CompletableFuture<String>> priceFutures = SHOPS.stream()
                .map(shop -> CompletableFuture.supplyAsync(
                        () -> String.format("%s price is %.2f",
                                shop.getName(),
                                shop.getPrice(product)), EXECUTOR))
                .collect(Collectors.toList());
        return priceFutures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
    }

    public static List<String> findPricesParallel(String product) {
        return SHOPS.parallelStream()
                .map(shop -> String.format("%s price is %.2f",
                        shop.getName(),
                        shop.getPrice(product)))
                .collect(Collectors.toList());
    }

    public static List<String> findPrices(String product) {
        return SHOPS.stream()
                .map(shop -> String.format("%s price is %.2f",
                        shop.getName(),
                        shop.getPrice(product)))
                .collect(Collectors.toList());
    }

    private static void demoWithFuture() {
        Shop shop = new Shop("BestShop");
        long start = System.nanoTime();
        Future<Double> futurePrice = shop.getPriceAsync("my favorite product");
        long invocationTime = (System.nanoTime() - start) / 1_000_000;
        System.out.println("Invocation return after " + invocationTime + " msecs");
        // Do some more tasks, like querying other shops
        doSomethingElse();
        try {
            double price = futurePrice.get();
            System.out.printf("Price is %.2f%n", price);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        long retrievalTime = (System.nanoTime() - start) / 1_000_000;
        System.out.println("Price returned after " + retrievalTime + " msecs");
    }

    private static void beforeJavaEight() {
        ExecutorService executor = Executors.newCachedThreadPool();
        Future<Double> future = executor.submit(new Callable<Double>() {
            @Override public Double call() throws Exception {
                return doSomeLongComputation();
            }
        });
        doSomethingElse();

        try {
            Double result = future.get(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            // the current thread was interrupted while waiting
        } catch (ExecutionException e) {
            // the computation throw an exception
        } catch (TimeoutException e) {
            // the timeout expired before the Future completion
        }
    }

    public static double doSomeLongComputation() {
        return 0.0;
    }

    public static void doSomethingElse() {
    }

    public static void delay() {
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}

class Shop {
    private String name;
    private Random random = new Random();

    public Shop(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getPrice(String product) {
        double price = calculatePrice(product);
        Discount.Code code = Discount.Code.values()[
                random.nextInt(Discount.Code.values().length)];
        return String.format("%s:%.2f:%s", name, price, code);
    }

    public Future<Double> getPriceAsync(String product) {
        // CompletableFuture static util method
        return CompletableFuture.supplyAsync(() -> calculatePrice(product));
        /*CompletableFuture<Double> futurePrice = new CompletableFuture<>();
        new Thread(() -> {
            try {
                double price = calculatePrice(product);
                futurePrice.complete(price);
            } catch (Exception e) {
                futurePrice.completeExceptionally(e);
            }
        }).start();
        return futurePrice;*/
    }

    private double calculatePrice(String product) {
        FutureDemo.delay();
        return random.nextDouble() * product.charAt(0) + product.charAt(1);
    }
}

class Discount {
    public enum Code {
        NONE(0), SLIVER(5), GOLD(10), PLATINUM(15), DIAMOND(20);
        private final int percentage;

        Code(int percentage) {
            this.percentage = percentage;
        }
    }

    public static String applyDiscount(Quote quote) {
        return quote.getShopName() + " price is " +
                Discount.apply(quote.getPrice(), quote.getDiscCode());
    }

    public static double apply(double price, Code code) {
        FutureDemo.delay();
        return price * (100 - code.percentage) / 100;
    }
}

class Quote {
    private final String shopName;
    private final double price;
    private final Discount.Code discCode;

    public Quote(String shopName, double price, Discount.Code discCode) {
        this.shopName = shopName;
        this.price = price;
        this.discCode = discCode;
    }

    public static Quote parse(String s) {
        String[] spilt = s.split(":");
        String shopName = spilt[0];
        double price = Double.parseDouble(spilt[1]);
        Discount.Code disCode = Discount.Code.valueOf(spilt[2]);
        return new Quote(shopName, price, disCode);
    }

    public String getShopName() {
        return shopName;
    }

    public double getPrice() {
        return price;
    }

    public Discount.Code getDiscCode() {
        return discCode;
    }
}