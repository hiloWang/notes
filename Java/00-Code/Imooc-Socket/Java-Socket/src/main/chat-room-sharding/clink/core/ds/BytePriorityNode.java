package clink.core.ds;

/**
 * 带优先级的节点, 可用于构成链表
 *
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 2018/11/25 23:04
 */
public class BytePriorityNode<Item> {

    public byte priority;
    public Item item;
    public BytePriorityNode<Item> next;//下一跳

    public BytePriorityNode(Item item) {
        this.item = item;
    }

    public void appendWithPriority(BytePriorityNode<Item> node) {
        if (next == null) {
            next = node;
        } else {
            BytePriorityNode<Item> after = this.next;
            //根据优先级添加
            if (after.priority < node.priority) {
                // 中间位置插入
                this.next = node;
                node.next = after;
            } else {
                //往后插入
                after.appendWithPriority(node);
            }
        }
    }

}
