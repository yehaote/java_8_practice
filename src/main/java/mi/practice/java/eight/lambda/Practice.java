package mi.practice.java.eight.lambda;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Practice {
    public static void main(String[] args) {
        List<Apple> inventory = new ArrayList<>();
        // before java 8
        inventory.sort(new AppleComparator());
        // anonymous class
        inventory.sort(new Comparator<Apple>() {
            @Override public int compare(Apple o1, Apple o2) {
                return o1.getWeight().compareTo(o2.getWeight());
            }
        });
        // lambda(passing code)
        inventory.sort((Apple a1, Apple a2) -> a1.getWeight().compareTo(a2.getWeight()));
        // lambda ab.
        inventory.sort((a1, a2) -> a1.getWeight().compareTo(a2.getWeight()));
        inventory.sort(Comparator.comparing(a -> a.getWeight()));
        inventory.sort(Comparator.comparing(Apple::getWeight));
    }
}

class AppleComparator implements Comparator<Apple> {
    @Override
    public int compare(Apple a1, Apple a2) {
        return a1.getWeight().compareTo(a2.getWeight());
    }
}
