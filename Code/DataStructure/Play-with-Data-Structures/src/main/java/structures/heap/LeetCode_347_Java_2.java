package structures.heap;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * https://leetcode-cn.com/problems/top-k-frequent-elements/description/
 *
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 2018/10/14 16:28
 */
public class LeetCode_347_Java_2 {

    /**
     * 前K个高频元素
     */
    public List<Integer> topKFrequent(int[] nums, int k) {
        Map<Integer, Integer> map = new TreeMap<>();

        for (int num : nums) {
            if (map.containsKey(num)) {
                map.put(num, map.get(num) + 1);
            } else {
                map.put(num, 1);
            }
        }

        //频次越小，优先级越低
        Comparator<Integer> comparator = Comparator.comparingInt(map::get);

        java.util.Queue<Integer> queue = new java.util.PriorityQueue<>(comparator);

        for (Map.Entry<Integer,Integer> entry : map.entrySet()) {
            if (queue.size() < k) {
                //先入队k个
                queue.add(entry.getKey());
            } else if (map.get(queue.peek()) <entry.getValue()/*新的元素的频次比如队列中频次最小的要大则替换*/) {
                queue.remove();
                queue.add(entry.getKey());
            }
        }

        LinkedList<Integer> integers = new LinkedList<>();
        while (!queue.isEmpty()) {
            integers.addFirst(queue.remove());
        }
        return integers;
    }

}
