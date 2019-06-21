package com.bennyhuo.github.view

import android.content.Intent
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import com.bennyhuo.experimental.coroutines.launchUI
import com.bennyhuo.github.R
import com.bennyhuo.github.common.ext.no
import com.bennyhuo.github.common.ext.otherwise
import com.bennyhuo.github.common.ext.yes
import com.bennyhuo.github.model.account.AccountManager
import com.bennyhuo.github.model.account.OnAccountStateChangeListener
import com.bennyhuo.github.network.entities.User
import com.bennyhuo.github.utils.afterClosed
import com.bennyhuo.github.utils.showFragment
import com.bennyhuo.github.view.config.NavViewItem
import com.bennyhuo.github.view.config.Themer
import com.bennyhuo.github.view.config.Themer.ThemeMode.DAY
import com.bennyhuo.github.view.widget.ActionBarController
import com.bennyhuo.github.view.widget.NavigationController
import com.bennyhuo.github.view.widget.confirm
import com.bennyhuo.tieguanyin.annotations.ActivityBuilder
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.menu_item_daynight.view.*
import org.jetbrains.anko.sdk15.listeners.onCheckedChange
import org.jetbrains.anko.toast

@ActivityBuilder(flags = [Intent.FLAG_ACTIVITY_CLEAR_TOP])
class MainActivity : AppCompatActivity(), OnAccountStateChangeListener {

    val actionBarController by lazy {
        ActionBarController(this)
    }

    private val navigationController by lazy{
        NavigationController(navigationView, ::onNavItemChanged, ::handleNavigationHeaderClickEvent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Themer.applyProperTheme(this)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.setDrawerListener(toggle)
        toggle.syncState()

        initNavigationView()

        AccountManager.onAccountStateChangeListeners.add(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        AccountManager.onAccountStateChangeListeners.remove(this)
    }

    private fun initNavigationView() {
        AccountManager.isLoggedIn()
                .yes {
                    navigationController.useLoginLayout()
                }
                .otherwise {
                    navigationController.useNoLoginLayout()
                }
        navigationController.selectProperItem()
    }

    override fun onLogin(user: User) {
        navigationController.useLoginLayout()
    }

    override fun onLogout() {
        navigationController.useNoLoginLayout()
    }

    private fun onNavItemChanged(navViewItem: NavViewItem){
        drawer_layout.afterClosed {
            showFragment(R.id.fragmentContainer, navViewItem.fragmentClass, navViewItem.arguements)
            title = navViewItem.title
        }
    }

    private fun handleNavigationHeaderClickEvent(){
        AccountManager.isLoggedIn().no {
            startLoginActivity()
        }.otherwise {
            launchUI {
                if(confirm("提示", "确认注销吗?")){
                    AccountManager
                            .logout()
                            .subscribe ({
                                toast("注销成功")
                            }, {
                                it.printStackTrace()
                            })
                } else {
                    toast("取消了")
                }
            }
        }

    }

    override fun onBackPressed() {
        if(drawer_layout.isDrawerOpen(GravityCompat.START)){
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_actionbar, menu)
        menu.findItem(R.id.dayNight).actionView.dayNightSwitch.apply {
            isChecked = Themer.currentTheme() == DAY

            onCheckedChange { buttonView, isChecked ->
                Themer.toggle(this@MainActivity)
            }
        }
        return true
    }
}
