package com.bennyhuo.github.view.fragments.subfragments

import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.View
import com.bennyhuo.github.R
import com.bennyhuo.github.network.entities.Issue
import com.bennyhuo.github.utils.githubTimeToDate
import com.bennyhuo.github.utils.htmlText
import com.bennyhuo.github.utils.view
import com.bennyhuo.github.view.common.CommonListAdapter
import kotlinx.android.synthetic.main.item_issue.view.*
import org.jetbrains.anko.imageResource

/**
 * Created by benny on 7/9/17.
 */
open class IssueListAdapter : CommonListAdapter<Issue>(R.layout.item_issue) {
    override fun onItemClicked(itemView: View, issue: Issue) {
        // todo
    }

    override fun onBindData(viewHolder: ViewHolder, issue: Issue) {
        viewHolder.itemView.apply {
            iconView.imageResource = if (issue.state == "open") R.drawable.ic_issue_open else R.drawable.ic_issue_closed
            titleView.text = issue.title
            timeView.text = githubTimeToDate(issue.created_at).view()
            bodyView.htmlText = issue.body_html
            commentCount.text = issue.comments.toString()
        }
    }
}