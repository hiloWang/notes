package structures.linked_recursion;

/**
 * https://leetcode-cn.com/problems/remove-linked-list-elements/description/
 *
 * @author ztiany
 * Email: ztiany3@gmail.com
 */
public class LeetCode203_Solution3 implements Solution {

    @Override
    public ListNode removeElements(ListNode head, int val) {

        if (head == null) {//递归停止的条件，也就是递归逐步分解问题过程中最基本的解
            return null;
        }

        head.next = removeElements(head.next, val);/*用同样的算法解决更小范围的值*/

        return head.val == val ? head.next : head;/*这里就包含的移除节点的逻辑*/
    }

}
