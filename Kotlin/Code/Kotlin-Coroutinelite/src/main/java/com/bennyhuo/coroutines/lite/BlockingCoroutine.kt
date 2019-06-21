package com.bennyhuo.coroutines.lite

import java.util.concurrent.LinkedBlockingDeque
import kotlin.coroutines.experimental.CoroutineContext

typealias EventTask = () -> Unit

class BlockingQueueDispatcher : LinkedBlockingDeque<EventTask>(), Dispatcher {

    override fun dispatch(block: EventTask) {
        println("BlockingQueueDispatcher->dispatch->EventTask=$block ${block.hashCode()}")
        offer(block)
    }
}

class BlockingCoroutine<T>(context: CoroutineContext, private val eventQueue: LinkedBlockingDeque<EventTask>, block: suspend () -> T) : AbstractCoroutine<T>(context, block) {

    var count = 1

    fun joinBlocking() {
        while (!isCompleted) {
            println("BlockingQueueDispatcher->dispatch->isCompleted = $isCompleted")
            val method = eventQueue.take()
            println("BlockingQueueDispatcher->dispatch->get=$method ${method.hashCode()}")
            method.invoke()
        }
    }
}