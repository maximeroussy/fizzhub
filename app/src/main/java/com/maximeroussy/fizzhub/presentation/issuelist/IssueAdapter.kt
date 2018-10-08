package com.maximeroussy.fizzhub.presentation.issuelist

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.maximeroussy.fizzhub.R
import com.maximeroussy.fizzhub.databinding.ViewIssueItemBinding
import com.maximeroussy.fizzhub.domain.models.GithubIssue
import com.maximeroussy.fizzhub.presentation.issuelist.IssueAdapter.IssueViewHolder

class IssueAdapter(private val items: MutableList<GithubIssue>) : RecyclerView.Adapter<IssueViewHolder>() {
  private lateinit var clickListener: (GithubIssue) -> Unit

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IssueViewHolder {
    val layoutInflater = LayoutInflater.from(parent.context)
    val binding = DataBindingUtil.inflate<ViewIssueItemBinding>(layoutInflater, R.layout.view_issue_item, parent, false)
    return IssueViewHolder(binding)
  }

  override fun getItemCount(): Int {
    return items.size
  }

  override fun onBindViewHolder(viewHolder: IssueViewHolder, position: Int) {
    val item = items[position]
    viewHolder.bind(item, clickListener)
    val textColor = viewHolder.itemView.context.resources.getColor(getStateColor(item.state))
    viewHolder.stateText.setTextColor(textColor)
  }

  fun updateData(newData: List<GithubIssue>) {
    this.items.clear()
    this.items.addAll(newData)
    notifyDataSetChanged()
  }

  fun addData(newData: List<GithubIssue>) {
    newData.forEach {
      this.items.add(it)
      notifyItemInserted(items.indexOf(it))
    }
  }

  fun setOnClickListener(clickListener: (GithubIssue) -> Unit) {
    this.clickListener = clickListener
  }

  private fun getStateColor(state: String): Int {
    return when(state) {
      "open" -> R.color.open_green
      "closed" -> R.color.closed_red
      else -> R.color.colorPrimary
    }
  }

  class IssueViewHolder(private val binding: ViewIssueItemBinding) : RecyclerView.ViewHolder(binding.root) {
    val stateText = binding.issueState

    fun bind(item: GithubIssue, clickListener: (GithubIssue) -> Unit) {
      binding.issue = item
      binding.issueCard.setOnClickListener { clickListener(item) }
    }
  }
}
