package structures.heap;

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
public class LeetCode_347_Java_1 {

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

        java.util.Queue<Freq> queue = new java.util.PriorityQueue<>();

        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            if (queue.size() < k) {
                //先入队k个
                queue.add(new Freq(entry.getKey(), entry.getValue()));
            } else if (queue.peek().freq < entry.getValue()/*新的元素的频次比如队列中频次最小的要大则替换*/) {
                queue.remove();
                queue.add(new Freq(entry.getKey(), entry.getValue()));
            }
        }

        LinkedList<Integer> integers = new LinkedList<>();
        while (!queue.isEmpty()) {
            integers.addFirst(queue.remove().value);
        }

        return integers;
    }

    /**
     * 频次
     */
    private class Freq implements Comparable<Freq> {

        int value, freq;

        Freq(int value, int freq) {
            this.value = value;
            this.freq = freq;
        }

        /*使用的是Java的最小堆，此次实现与使用的堆有关*/
        @Override
        public int compareTo(Freq other) {
            //频次越小，优先级越低
            if (this.freq < other.freq) {
                return -1;
            } else if (this.freq > other.freq) {
                return 1;
            }
            return 0;
        }
    }

}
