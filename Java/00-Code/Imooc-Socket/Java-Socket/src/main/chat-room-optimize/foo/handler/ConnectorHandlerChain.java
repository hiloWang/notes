package foo.handler;

/**
 * 责任链默认结构封装
 *
 * @param <Model>
 */
public abstract class ConnectorHandlerChain<Model> {

    // 当前节点所持有的下一份节点
    private volatile ConnectorHandlerChain<Model> next;

    /**
     * 添加一个新的节点到当前链式结构的末尾
     *
     * @param newChain 新的节点
     * @return 返回新的节点
     */
    public ConnectorHandlerChain<Model> appendLast(ConnectorHandlerChain<Model> newChain) {
        // 添加前，优先判断是否就是当前实例，
        // 同时在一个链式结构中只能存在某一个节点的一个实例，使用Class区分
        if (newChain == this || this.getClass().equals(newChain.getClass())) {
            return this;
        }

        synchronized (this) {
            // 下一个节点为null，则追加
            if (next == null) {
                next = newChain;
                return newChain;
            }
            // 否则让下一个节点进行新节点的添加，把责任让后推
            return next.appendLast(newChain);
        }
    }

    /**
     * PS1：移除节点中的某一个节点及其之后节点
     * PS2：移除某节点时，如果其具有后续的节点，则把后续节点接到当节点上；实现可以移除中间某个节点
     *
     * @param clx 待移除节点的Class信息
     * @return 是否移除成功
     */
    public synchronized boolean remove(Class<? extends ConnectorHandlerChain<Model>> clx) {
        // 自己不能移除自己，因为自己未持有上一个链接的引用
        if (this.getClass().equals(clx)) {
            return false;
        }

        synchronized (this) {
            if (next == null) {
                // 当前无下一个节点存在，无法判断
                return false;
            } else if (next.getClass().equals(clx)) {
                // 移除next节点
                next = next.next;
                // A B C -> B
                // A C
                return true;
            } else {
                // 交给next进行移除操作
                return next.remove(clx);
            }
        }
    }

    /**
     * 优先自己消费，如果自己未消费；则给next消费
     * 若：next=null 或 next 未消费，则回调{@link #consumeAgain(ConnectorHandler, Object)} 尝试再次消费
     *
     * @param handler ClientHandler
     * @param model   Model
     * @return True consume 消费 或 consumeAgain 消费
     */
    synchronized boolean handle(ConnectorHandler handler, Model model) {
        ConnectorHandlerChain<Model> next = this.next;

        // 自己消费
        if (consume(handler, model)) {
            return true;
        }

        boolean consumed = next != null && next.handle(handler, model);
        if (consumed) {
            return true;
        }

        return consumeAgain(handler, model);
    }

    protected abstract boolean consume(ConnectorHandler handler, Model model);

    protected boolean consumeAgain(ConnectorHandler handler, Model model) {
        return false;
    }

}

