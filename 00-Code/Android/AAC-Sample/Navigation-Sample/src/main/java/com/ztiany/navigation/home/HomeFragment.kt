package com.ztiany.navigation.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.support.v4.UI

/**
 *
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2018-06-21 10:43
 */
class HomeFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return UI {

            linearLayout {
                orientation = LinearLayout.VERTICAL

                button("open Detail") {
                    onClick {
                        openDetail(this@button)
                    }
                }.lparams(width = wrapContent) {
                    horizontalMargin = dip(5)
                    topMargin = dip(10)
                }

                button("open Category") {
                    onClick {
                        openCategory(this@button)
                    }
                }.lparams(width = wrapContent) {
                    horizontalMargin = dip(5)
                    topMargin = dip(10)
                }

                button("open ProfileActivity") {
                    onClick {
                        openProfile(this@button)
                    }
                }.lparams(width = wrapContent) {
                    horizontalMargin = dip(5)
                    topMargin = dip(10)
                }

            }
        }.view
    }

    private fun openProfile(button: @AnkoViewDslMarker Button) {
        val data = Uri.parse("http://www.ztiany.me/profile/edit")
        val intent = Intent(Intent.ACTION_VIEW, data)
        startActivity(intent)
    }

    private fun openCategory(button: @AnkoViewDslMarker Button) {
        val action = HomeFragmentDirections.action_home_to_detail()
        action.setDetailId(100)
        Navigation.findNavController(button).navigate(action)
    }

    private fun openDetail(button: @AnkoViewDslMarker Button) {
        val action = HomeFragmentDirections.action_home_to_category("categoryName")
        action.setCategoryId(100)

        val navOptions = NavOptions.Builder().setEnterAnim(android.R.anim.fade_in)
                .setExitAnim(android.R.anim.fade_out)
                .build()

        Navigation.findNavController(button).navigate(action, navOptions)
    }

}