package mi.practice.java.eight.stream;

import java.util.*;
import java.util.stream.Collectors;

public class TradersAndTransactions {
    public static void main(String[] args) {
        Trader raoul = new Trader("Raoul", "Cambridge");
        Trader mario = new Trader("Mario", "Milan");
        Trader alan = new Trader("Alan", "Cambridge");
        Trader brian = new Trader("Brian", "Cambridge");
        List<Transaction> transactions = Arrays.asList(
                new Transaction(brian, 2011, 300),
                new Transaction(raoul, 2012, 1000),
                new Transaction(raoul, 2011, 400),
                new Transaction(mario, 2012, 710),
                new Transaction(mario, 2012, 700),
                new Transaction(alan, 2012, 950)
        );
        // 1.
        List<Transaction> result = transactions.stream()
                .filter(t -> t.getYear() == 2011)
                .sorted(Comparator.comparing(Transaction::getValue))
                .collect(Collectors.toList());
        System.out.println(result);
        // 2.
        List<String> cities = transactions.stream()
                .map(t -> t.getTrader().getCity())
                .distinct()
                .collect(Collectors.toList());
        System.out.println(cities);
        Set<String> citySet = transactions.stream()
                .map(t -> t.getTrader().getCity())
                .collect(Collectors.toSet());
        System.out.println(citySet);
        // 3.
        List<Trader> tradersFromCambridge = transactions.stream()
                .map(t -> t.getTrader())
                .filter(t -> "Cambridge".equals(t.getCity()))
                .distinct()
                .sorted(Comparator.comparing(Trader::getName))
                .collect(Collectors.toList());
        System.out.println(tradersFromCambridge);
        // 4.
        String names = transactions.stream()
                .map(t -> t.getTrader().getName())
                .distinct()
                .sorted()
                .reduce("", (name1, name2) -> (name1 + "," + name2));
        System.out.println(names);
        names = transactions.stream()
                .map(t -> t.getTrader().getName())
                .distinct()
                .sorted()
                .collect(Collectors.joining());
        System.out.println(names);
        // 5.
        boolean anyMilan = transactions.stream()
                .anyMatch(t -> "Milan".equals(t.getTrader().getCity()));
        System.out.println(anyMilan);
        // 6.
        transactions.stream()
                .filter(t -> "Cambridge".equals(t.getTrader().getCity()))
                .forEach(t -> System.out.println(t.getValue()));
        // 7.
        Optional<Transaction> maxTransaction = transactions.stream()
                .max(Comparator.comparing(Transaction::getValue));
        System.out.println(maxTransaction.get());
        System.out.println(transactions.stream()
                //.reduce((t1, t2) -> t1.getValue() > t2.getValue() ? t1 : t2).get());
                .map(Transaction::getValue)
                .reduce(Integer::max));
        //8.
        System.out.println(
                transactions.stream()
                        .min(Comparator.comparing(Transaction::getValue))
                        .get());
    }
}

class Trader {
    private final String name;
    private final String city;

    public Trader(String name, String city) {
        this.name = name;
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    @Override
    public String toString() {
        return "Trader:" + this.name + "in " + this.city;
    }
}

class Transaction {
    private final Trader trader;
    private final int year;
    private final int value;

    public Transaction(Trader trader, int year, int value) {
        this.trader = trader;
        this.year = year;
        this.value = value;
    }

    public Trader getTrader() {
        return trader;
    }

    public int getYear() {
        return year;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "{" + this.trader + ", year:" + this.year +
                ", value:" + this.value + "}";
    }
}