package com.bennyhuo.github.view.widget

import android.support.design.widget.NavigationView
import android.view.MenuItem
import com.bennyhuo.common.log.logger
import com.bennyhuo.github.R
import com.bennyhuo.github.model.account.AccountManager
import com.bennyhuo.github.network.entities.User
import com.bennyhuo.github.settings.Settings
import com.bennyhuo.github.utils.doOnLayoutAvailable
import com.bennyhuo.github.utils.loadWithGlide
import com.bennyhuo.github.utils.selectItem
import com.bennyhuo.github.view.config.NavViewItem
import kotlinx.android.synthetic.main.nav_header_main.view.*
import org.jetbrains.anko.imageResource
import org.jetbrains.anko.sdk15.listeners.onClick

class NavigationController(
        private val navigationView: NavigationView,
        private val onNavItemChanged: (NavViewItem) -> Unit,
        private val onHeaderClick: () -> Unit
) : NavigationView.OnNavigationItemSelectedListener {

    init {
        navigationView.setNavigationItemSelectedListener(this)
    }

    private var currentItem: NavViewItem? = null

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        navigationView.apply {
            Settings.lastPage = item.itemId
            val navItem = NavViewItem[item.itemId]
            onNavItemChanged(navItem)
        }
        return true
    }

    fun useLoginLayout() {
        navigationView.menu.clear()
        navigationView.inflateMenu(R.menu.activity_main_drawer) //inflate new items.
        onUpdate(AccountManager.currentUser)
        selectProperItem()
    }

    fun useNoLoginLayout() {
        navigationView.menu.clear()
        navigationView.inflateMenu(R.menu.activity_main_drawer_no_logged_in) //inflate new items.
        onUpdate(AccountManager.currentUser)
        selectProperItem()
    }

    private fun onUpdate(user: User?) {
        navigationView.doOnLayoutAvailable {
            navigationView.apply {
                usernameView.text = user?.login ?: "请登录"
                emailView.text = user?.email ?: "bennyhuo@kotliner.cn"
                if (user == null) {
                    avatarView.imageResource = R.drawable.ic_github
                } else {
                    avatarView.loadWithGlide(user.avatar_url, user.login.first())
                }

                navigationHeader.onClick { onHeaderClick() }
            }
        }
    }

    fun selectProperItem() {
        logger.debug("selectProperItem")
        navigationView.doOnLayoutAvailable {
            logger.debug("selectProperItem onLayout: $currentItem")
            ((currentItem?.let { NavViewItem[it] } ?: Settings.lastPage)
                    .takeIf { navigationView.menu.findItem(it) != null } ?: run { Settings.defaultPage })
                    .let(navigationView::selectItem)
        }
    }
}