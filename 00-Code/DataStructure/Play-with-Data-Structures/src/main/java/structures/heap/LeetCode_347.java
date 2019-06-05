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
public class LeetCode_347 {

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

        PriorityQueue<Freq> queue = new PriorityQueue<>();

        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            if (queue.getSize() < k) {
                //先入队k个
                queue.enqueue(new Freq(entry.getKey(), entry.getValue()));
            } else if (queue.getFront().freq < entry.getValue()/*新的元素的频次比如队列中频次最小的要大则替换*/) {
                queue.dequeue();
                queue.enqueue(new Freq(entry.getKey(), entry.getValue()));
            }
        }

        LinkedList<Integer> integers = new LinkedList<>();
        while (!queue.isEmpty()) {
            integers.addFirst(queue.dequeue().value);
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

        /*使用的是最大堆，此次实现与使用的堆有关*/
        @Override
        public int compareTo(Freq other) {
            //凭此越小，优先级越高
            if (this.freq < other.freq) {
                return 1;
            } else if (this.freq > other.freq) {
                return -1;
            }
            return 0;
        }
    }

}
