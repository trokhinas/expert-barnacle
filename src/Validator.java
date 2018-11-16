import java.util.*;

public class Validator {

    public static boolean isOrdered(List<? extends Comparable> a) {
        if(a.isEmpty() || a.size() == 1)
            return true;

        Iterator<? extends Comparable> it = a.iterator();
        Comparable prev = it.next(), next;

        do {
            next = it.next();
            if(prev.compareTo(next) >= 0)//строгое возрастание
                return false;
            prev = next;
        }while (it.hasNext());
        return true;
    }
    public static boolean isIncluded(List<Double> a, Double x) {

        //надо поработать
        //это все происходит при условии что массив упорядочен
        if(!isOrdered(a)) throw new IllegalArgumentException("Array must be ordered");
        return x >= a.get(0) && x <= a.get(a.size() - 1) && a.size() != 1;
    }

}