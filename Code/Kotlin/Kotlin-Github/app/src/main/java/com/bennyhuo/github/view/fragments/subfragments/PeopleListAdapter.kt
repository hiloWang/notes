package com.bennyhuo.github.view.fragments.subfragments

import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.View
import com.bennyhuo.github.R.layout
import com.bennyhuo.github.network.entities.User
import com.bennyhuo.github.utils.loadWithGlide
import com.bennyhuo.github.view.common.CommonListAdapter
import kotlinx.android.synthetic.main.item_user.view.*

/**
 * Created by benny on 7/9/17.
 */
class PeopleListAdapter : CommonListAdapter<User>(layout.item_user) {
    override fun onItemClicked(itemView: View, item: User) {
        // todo
    }

    override fun onBindData(viewHolder: ViewHolder, user: User) {
        viewHolder.itemView.apply {
            avatarView.loadWithGlide(user.avatar_url, user.login.first())
            nameView.text = user.login
        }
    }
}