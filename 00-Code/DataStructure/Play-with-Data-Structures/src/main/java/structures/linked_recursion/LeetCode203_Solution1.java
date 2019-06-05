package structures.linked_recursion;

/**
 * https://leetcode-cn.com/problems/remove-linked-list-elements/description/
 *
 * @author ztiany
 * Email: ztiany3@gmail.com
 */
public class LeetCode203_Solution1 implements Solution{

    @Override
    public ListNode removeElements(ListNode head, int val) {
        //step 1：对于头节点需要进行特别的判断
        while (head != null && head.val == val) {
            head = head.next;
        }

        //step 2：对头节点进行判空
        if (head == null) {
            return null;
        }

        //step 3：遍历头节点之后的
        ListNode prev = head;
        while (prev.next != null) {
            if (prev.next.val == val) {//删除符合条件的节点
                prev.next = prev.next.next;
            } else {//转到下一个节点
                prev = prev.next;
            }
        }

        return head;
    }

}
