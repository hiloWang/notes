package structures.linked_recursion;

/**
 * @author ztiany
 * Email: ztiany3@gmail.com
 */
public class Main {

    public static void main(String... args) {
        testSolution(new LeetCode203_Solution1());
        testSolution(new LeetCode203_Solution2());
        testSolution(new LeetCode203_Solution3());
        System.out.println(sum(new int[]{1, 2, 3, 4, 5, 6,}, 0));
    }

    private static int sum(int[] arr, int l) {
        if (l == arr.length) {//递归定制条件
            return 0;
        }
        int x = sum(arr, l + 1);//递归问题分解
        return arr[l] + x;//组合返回
    }

    private static void testSolution(Solution solution) {
        System.out.println("test -------------------------->" + solution.getClass().getSimpleName());
        int[] arr = {1, 2, 3, 4, 3, 5, 3, 6, 3, 7, 3, 3, 3, 8, 3};

        ListNode head = new ListNode(arr);
        System.out.println(head);

        head = solution.removeElements(head, 3);
        System.out.println(head);
    }

}
