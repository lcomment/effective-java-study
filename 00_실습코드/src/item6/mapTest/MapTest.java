package item6.mapTest;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MapTest {
    public static void main(String[] args) {
        Map<Integer, String> map = new HashMap<>();

        map.put(1, "k");
        map.put(2, "o");

        Set<Integer> set1 = map.keySet();
        System.out.println(System.identityHashCode(set1));

        map.put(3, "m");
        map.put(4, "m");

        Set<Integer> set2 = map.keySet();
        System.out.println(System.identityHashCode(set2));
    }
}
