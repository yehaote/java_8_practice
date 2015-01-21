package chap1;

import java.util.Arrays;
import java.util.Random;

public class Ex1 {
    public static void main(String[] args) {
        Integer size = 10;
        Random random = new Random();
        Integer[] data = new Integer[size];
        for (int i = 0; i < size; i++) {
            data[i] = random.nextInt(20);
        }
        System.out.println("main:" + Thread.currentThread());
        Arrays.sort(data, (Integer a, Integer b) -> {
            System.out.println("lambda:" + Thread.currentThread());
            if (a > b) {
                return -1;
            } else if (a < b) {
                return 1;
            } else {
                return 0;
            }
        });
    }
}
