package com.bennyhuo.github.utils

import android.support.v4.view.PagerAdapter

/**
 * Created by benny on 7/9/17.
 */
class ViewPagerAdapterList<T>(val adapter: PagerAdapter): ArrayList<T>(){
    override fun removeAt(index: Int): T {
        return super.removeAt(index).apply { adapter.notifyDataSetChanged() }
    }

    override fun remove(element: T): Boolean {
        val index = indexOf(element)
        return super.remove(element).apply { adapter.notifyDataSetChanged() }
    }

    override fun removeRange(fromIndex: Int, toIndex: Int) {
        super.removeRange(fromIndex, toIndex).apply { adapter.notifyDataSetChanged() }
    }

    override fun removeAll(elements: Collection<T>): Boolean {
        return super.removeAll(elements).apply { adapter.notifyDataSetChanged() }
    }

    override fun add(index: Int, element: T) {
        super.add(index, element)
        adapter.notifyDataSetChanged()
    }

    override fun add(element: T): Boolean {
        return super.add(element).apply {
            adapter.notifyDataSetChanged()
        }

    }

    override fun addAll(elements: Collection<T>): Boolean {
        return super.addAll(elements).apply { adapter.notifyDataSetChanged() }
    }

    override fun addAll(index: Int, elements: Collection<T>): Boolean {
        return super.addAll(index, elements).apply { adapter.notifyDataSetChanged() }
    }

    fun update(elements: Collection<T>){
        super.clear()
        super.addAll(elements)
        adapter.notifyDataSetChanged()
    }
}