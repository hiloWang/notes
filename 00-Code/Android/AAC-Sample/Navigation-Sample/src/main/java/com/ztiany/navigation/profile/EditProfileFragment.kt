package com.ztiany.navigation.profile

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import org.jetbrains.anko.dip
import org.jetbrains.anko.linearLayout
import org.jetbrains.anko.margin
import org.jetbrains.anko.support.v4.UI
import org.jetbrains.anko.textView

/**
 *
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2018-06-21 11:54
 */
class EditProfileFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return UI {
            linearLayout {
                orientation = LinearLayout.VERTICAL

                textView("Edit Profile Fragment").lparams() {
                    margin = dip(10)
                }

            }
        }.view
    }

}