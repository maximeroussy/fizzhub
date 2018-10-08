package com.maximeroussy.fizzhub.presentation.repositorylist

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.maximeroussy.fizzhub.R
import com.maximeroussy.fizzhub.databinding.ViewRepositoryItemBinding
import com.maximeroussy.fizzhub.domain.models.GithubRepository
import com.maximeroussy.fizzhub.presentation.repositorylist.RepositoryAdapter.RepositorySearchViewHolder

class RepositoryAdapter(
    private val glide: RequestManager,
    private val items: MutableList<GithubRepository>
) : RecyclerView.Adapter<RepositorySearchViewHolder>() {
  private lateinit var clickListener: (Int, GithubRepository) -> Unit
  private var longClickListener: ((Int) -> Unit)? = null
  private val selectedItems = SparseBooleanArray()

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositorySearchViewHolder {
    val layoutInflater = LayoutInflater.from(parent.context)
    val binding = DataBindingUtil.inflate<ViewRepositoryItemBinding>(layoutInflater, R.layout.view_repository_item,
        parent, false)
    return RepositorySearchViewHolder(binding)
  }

  override fun getItemCount(): Int {
    return items.size
  }

  override fun onBindViewHolder(viewHolder: RepositorySearchViewHolder, position: Int) {
    val item = items[position]
    viewHolder.bind(item, clickListener, longClickListener)
    if (selectedItems.get(position, false)) {
      viewHolder.cardView.setBackgroundResource(R.color.selected_card_background)
    } else {
      viewHolder.cardView.setBackgroundResource(R.color.white)
    }
    glide.load(item.avatarUrl).apply(RequestOptions().error(R.drawable.placeholder)).into(viewHolder.image)
  }

  fun updateData(newData: List<GithubRepository>) {
    this.items.clear()
    this.items.addAll(newData)
    notifyDataSetChanged()
  }

  fun addData(newData: List<GithubRepository>) {
    newData.forEach {
      this.items.add(it)
      notifyItemInserted(items.indexOf(it))
    }
  }

  fun removeItems(itemsToRemove: List<GithubRepository>): Boolean {
    itemsToRemove.forEach {
      val position = this.items.indexOf(it)
      this.items.removeAt(position)
      notifyItemRemoved(position)
    }
    return this.items.isEmpty()
  }

  fun setOnClickListener(clickListener: (Int, GithubRepository) -> Unit) {
    this.clickListener = clickListener
  }

  fun setOnLongClickListener(longClickListener: (Int) -> Unit) {
    this.longClickListener = longClickListener
  }

  fun select(position: Int) {
    if (selectedItems.get(position, false)) {
      selectedItems.delete(position)
    } else {
      selectedItems.put(position, true)
    }
    notifyItemChanged(position)
  }

  fun clearSelections() {
    selectedItems.clear()
    notifyDataSetChanged()
  }

  fun getSelectedItemCount(): Int {
    return selectedItems.size()
  }

  fun getSelectedItems(): List<GithubRepository> {
    val selectedWords = ArrayList<GithubRepository>()
    for(i in items.indices) {
      if (selectedItems.get(i, false)) {
        selectedWords.add(items[i])
      }
    }
    return selectedWords
  }

  class RepositorySearchViewHolder(
      private val binding: ViewRepositoryItemBinding) : RecyclerView.ViewHolder(binding.root) {

    val image = binding.repositoryAvatar
    val cardView = binding.repositoryCard

    fun bind(item: GithubRepository, clickListener: (Int, GithubRepository) -> Unit, longClickListener: ((Int) ->
    Unit)?) {
      binding.repository = item
      binding.repositoryCard.setOnClickListener { clickListener(adapterPosition, item) }
      binding.repositoryCard.setOnLongClickListener { _ ->
        longClickListener?.let {
          longClickListener(adapterPosition)
        }
        true
      }
    }
  }
}
