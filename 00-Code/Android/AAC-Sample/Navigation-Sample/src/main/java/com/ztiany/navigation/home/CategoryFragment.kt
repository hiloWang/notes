package com.ztiany.navigation.home

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout.VERTICAL
import org.jetbrains.anko.dip
import org.jetbrains.anko.linearLayout
import org.jetbrains.anko.margin
import org.jetbrains.anko.support.v4.UI
import org.jetbrains.anko.textView

/**
 *
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2018-06-21 10:43
 */
class CategoryFragment : Fragment() {

    private val args by lazy {
        CategoryFragmentArgs.fromBundle(arguments)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return UI {
            linearLayout {
                orientation = VERTICAL

                textView("Category Fragment").lparams() {
                    margin = dip(10)
                }
                textView("Category id: " + args.categoryId).lparams() {
                    margin = dip(10)
                }
                textView("Category name: " + args.categoryName).lparams() {
                    margin = dip(10)
                }
            }
        }.view
    }

}