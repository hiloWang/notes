package structures.linked_recursion;

public class ListNode {

    int val;
    ListNode next;

    ListNode(int x) {
        val = x;
    }

    ListNode(int[] arr) {
        this.val = arr[0];
        int length = arr.length;
        ListNode cur = this;
        for (int i = 1; i < length; i++) {
            cur.next = new ListNode(arr[i]);
            cur = cur.next;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("ListNode：");
        ListNode cur = this;
        while (cur != null) {
            sb.append(cur.val).append("-->");
            cur = cur.next;
        }
        sb.append("NULL");
        return sb.toString();
    }
}