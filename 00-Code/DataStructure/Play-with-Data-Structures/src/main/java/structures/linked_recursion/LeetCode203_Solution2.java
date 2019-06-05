package structures.linked_recursion;

/**
 * https://leetcode-cn.com/problems/remove-linked-list-elements/description/
 *
 * @author ztiany
 * Email: ztiany3@gmail.com
 */
public class LeetCode203_Solution2 implements Solution {

    @Override
    public ListNode removeElements(ListNode head, int val) {
        //step 1 ：构建虚拟头节点
        ListNode dummyNode = new ListNode(-1);
        dummyNode.next = head;

        //step 2：遍历头节点之后的
        ListNode prev = dummyNode;
        while (prev.next != null) {
            if (prev.next.val == val) {//删除符合条件的节点
                prev.next = prev.next.next;
            } else {//转到下一个节点
                prev = prev.next;
            }
        }

        return dummyNode.next;
    }

}
