package com.bennyhuo.github.view.fragments.subfragments

import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.View
import com.bennyhuo.github.R
import com.bennyhuo.github.network.entities.Repository
import com.bennyhuo.github.utils.loadWithGlide
import com.bennyhuo.github.utils.toKilo
import com.bennyhuo.github.view.common.CommonListAdapter
import com.bennyhuo.github.view.startRepoDetailActivity
import kotlinx.android.synthetic.main.item_repo.view.*

class RepoListAdapter: CommonListAdapter<Repository>(R.layout.item_repo) {
    override fun onBindData(viewHolder: ViewHolder, repository: Repository) {
        viewHolder.itemView.apply {
            avatarView.loadWithGlide(repository.owner.avatar_url, repository.owner.login.first())
            repoNameView.text = repository.name
            descriptionView.text = repository.description
            langView.text = repository.language ?: "Unknown"
            starView.text = repository.stargazers_count.toKilo()
            forkView.text = repository.forks_count.toKilo()
        }
    }

    override fun onItemClicked(itemView: View, item: Repository) {
        itemView.startRepoDetailActivity(item)
    }

}