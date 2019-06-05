package structures.map;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 2018/10/14 10:05
 */
public class LeetCode_350 {

    public int[] intersect(int[] nums1, int[] nums2) {
        java.util.Map<Integer, Integer> map = new TreeMap<>();
        for (int i : nums1) {
            if (!map.containsKey(i)) {
                map.put(i, 1);
            }else {
                map.put(i, map.get(i) + 1);
            }
        }

        List<Integer> list = new ArrayList<>();

        for (int i : nums2) {
            if (map.containsKey(i)) {
                list.add(i);
                int value = map.get(i) - 1;
                if (value == 0) {
                    map.remove(i);
                }else {
                    map.put(i, value);
                }
            }
        }

        int[] result = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            result[i] = list.get(i);
        }
        return result;
    }

}
