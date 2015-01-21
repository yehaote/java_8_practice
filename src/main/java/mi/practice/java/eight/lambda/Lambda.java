package mi.practice.java.eight.lambda;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Lambda
 * Created by ner6 on 2015/1/20.
 */
public class Lambda {
    public static void demo1(){
        List<String> strings = new ArrayList<>();
        Comparator<String> stringLengthComparator = (String first, String second)->{
            if(first.length() < second.length()){
                return -1;
            } else if(first.length() > second.length()){
                return  1;
            }else {
                return 0;
            }
        };
        strings.sort(stringLengthComparator);

    }
}
