package structures.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 2018/10/14 0:17
 */
public class LeetCode_349 {

    public int[] intersection(int[] nums1, int[] nums2) {
        HashMap<Integer, Integer> map = new HashMap<>();

        for (int i : nums1) {
            map.put(i, 1);
        }

        for (int i : nums2) {
            if (map.containsKey(i)) {
                map.put(i, map.get(i) + 1);
            } else {
                map.put(i, 1);
            }
        }

        List<Integer> keys = new ArrayList<>(map.keySet());
        ListIterator<Integer> integerListIterator = keys.listIterator();
        while (integerListIterator.hasNext()) {
            Integer next = integerListIterator.next();
            if (map.get(next) < 2) {
                integerListIterator.remove();
            }
        }
        int[] result = new int[keys.size()];
        for (int i = 0; i < keys.size(); i++) {
            result[i] = keys.get(i);
        }
        return result;
    }

    public int[] intersection2(int[] nums1, int[] nums2) {
        Set<Integer> set = new HashSet<>();
        for (int i : nums1) {
            set.add(i);
        }

        List<Integer> list = new ArrayList<>();
        for (int i : nums2) {
            if (set.contains(i)) {
                list.add(i);
                set.remove(i);
            }
        }

        int[] result = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            result[i] = list.get(i);
        }
        return result;
    }

}
