package me.ztiany.rx

import io.reactivex.Maybe
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject

/**
 *
 * @author ztiany
 *          Email: ztiany3@gmail.com
 */

fun main(args: Array<String>) {
    sample1()
    sample2()
}

fun sample2() {
    val maybeEmpty: Maybe<Int> = Maybe.empty()
    //subscribeBy是RxKotlin扩展
    maybeEmpty.subscribeBy(
            onComplete = { println("Completed Empty") },
            onError = { println("Error $it") },
            onSuccess = { println("Completed with value $it") }
    )
}

fun sample1() {

    fun isEvenOrOdd(n: Int): String = if ((n % 2) == 0) "Even" else "Odd"

    val subject: Subject<Int> = PublishSubject.create()

    subject.map(
            {
                isEvenOrOdd(it)
            })
            .subscribe(
                    {
                        println("The number is $it")
                    }
            )

    subject.onNext(4)
    subject.onNext(9)
}
