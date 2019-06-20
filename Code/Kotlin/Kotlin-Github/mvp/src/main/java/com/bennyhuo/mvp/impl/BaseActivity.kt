package com.bennyhuo.mvp.impl

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.bennyhuo.mvp.IMvpView
import com.bennyhuo.mvp.IPresenter
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import kotlin.coroutines.experimental.buildSequence
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.jvmErasure

abstract class BaseActivity<out P : BasePresenter<BaseActivity<P>>> : AppCompatActivity(), IMvpView<P> {

    final override val presenter: P

    init {
        presenter = createPresenterKt()
        presenter.view = this
    }

    private fun createPresenterKt(): P {
        buildSequence {
            var thisClass: KClass<*> = this@BaseActivity::class
            while (true) {
                yield(thisClass.supertypes)
                thisClass = thisClass.supertypes.firstOrNull()?.jvmErasure ?: break
            }
        }.flatMap {
            it.flatMap { it.arguments }.asSequence()
        }.first {
                    it.type?.jvmErasure?.isSubclassOf(IPresenter::class) ?: false
                }.let {
                    return it.type!!.jvmErasure.primaryConstructor!!.call() as P
                }
    }

    private fun createPresenter(): P {
        buildSequence<Type> {
            var thisClass: Class<*> = this@BaseActivity.javaClass
            while (true) {
                yield(thisClass.genericSuperclass)
                thisClass = thisClass.superclass ?: break
            }
        }.filter {
            it is ParameterizedType
        }.flatMap {
                    (it as ParameterizedType).actualTypeArguments.asSequence()
                }.first {
                    it is Class<*> && IPresenter::class.java.isAssignableFrom(it)
                }.let {
                    return (it as Class<P>).newInstance()
                }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.onCreate(savedInstanceState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {}

    override fun onStart() {
        super.onStart()
        presenter.onStart()
    }

    override fun onResume() {
        super.onResume()
        presenter.onResume()
    }

    override fun onPause() {
        super.onPause()
        presenter.onPause()
    }

    override fun onStop() {
        super.onStop()
        presenter.onStop()
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        presenter.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        onViewStateRestored(savedInstanceState)
        presenter.onViewStateRestored(savedInstanceState)
    }
}
