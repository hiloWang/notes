package com.bennyhuo.github.view.config

import android.os.Bundle
import android.support.annotation.DrawableRes
import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import com.bennyhuo.github.R.drawable
import com.bennyhuo.github.R.id
import com.bennyhuo.github.view.fragments.AboutFragment
import com.bennyhuo.github.view.fragments.MyIssueFragment
import com.bennyhuo.github.view.fragments.PeopleFragment
import com.bennyhuo.github.view.fragments.RepoFragment
import com.bennyhuo.github.view.fragments.subfragments.RepoListFragmentBuilder

class NavViewItem private constructor(val groupId: Int = 0, val title: String, @DrawableRes val icon: Int, val fragmentClass: Class<out Fragment>, val arguements: Bundle = Bundle()){

    companion object{
        private val items = mapOf(
                id.navRepos to NavViewItem(0, "Repository", drawable.ic_repository, RepoFragment::class.java, Bundle().apply { putParcelable(RepoListFragmentBuilder.OPTIONAL_USER, null) }),
                id.navPeople to NavViewItem(0, "People", drawable.ic_people, PeopleFragment::class.java),
                id.navIssue to NavViewItem(0, "Issue", drawable.ic_issue, MyIssueFragment::class.java),
                id.navAbout to NavViewItem(0, "About", drawable.ic_about_us, AboutFragment::class.java)
        )

        operator fun get(@IdRes navId: Int): NavViewItem {
            return items[navId]?: items[id.navRepos]!!
        }

        operator fun get(item: NavViewItem): Int{
            return items.filter { it.value == item }.keys.first()
        }
    }

    override fun toString(): String {
        return "NavViewItem(groupId=$groupId, title='$title', icon=$icon, fragmentClass=$fragmentClass, arguements=$arguements)"
    }
}
