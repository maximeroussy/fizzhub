package com.maximeroussy.fizzhub.presentation.repositorylist

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.view.ActionMode
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.bumptech.glide.Glide
import com.maximeroussy.fizzhub.MainApplication
import com.maximeroussy.fizzhub.R
import com.maximeroussy.fizzhub.databinding.FragmentRepositoryListBinding
import com.maximeroussy.fizzhub.domain.models.GithubRepository
import com.maximeroussy.fizzhub.presentation.BaseFragment
import com.maximeroussy.fizzhub.presentation.repositorydetail.RepositoryDetailActivity
import com.maximeroussy.fizzhub.presentation.repositorysearch.RepositorySearchActivity
import com.maximeroussy.fizzhub.util.ViewModelFactory
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter
import jp.wasabeef.recyclerview.animators.ScaleInAnimator
import javax.inject.Inject

class RepositoryListFragment : BaseFragment<FragmentRepositoryListBinding>(R.layout.fragment_repository_list) {
  @Inject internal lateinit var viewModelFactory: ViewModelFactory<RepositoryListViewModel>
  private lateinit var viewModel: RepositoryListViewModel
  private lateinit var adapter: RepositoryAdapter
  private lateinit var swipeRefreshLayout: SwipeRefreshLayout

  private var actionMode: ActionMode? = null

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    viewModel = ViewModelProviders.of(this, viewModelFactory).get(RepositoryListViewModel::class.java)
    binding.viewModel = viewModel
    setupObservers()
    setupRecyclerView()
    setupSwipeRefresh()
  }

  override fun onAttach(context: Context?) {
    (context?.applicationContext as MainApplication).component.inject(this)
    super.onAttach(context)
  }

  override fun onResume() {
    super.onResume()
    viewModel.loadRepositories()
  }

  private fun setupRecyclerView() {
    val recyclerView = binding.recyclerView
    recyclerView.layoutManager = LinearLayoutManager(activity)
    recyclerView.itemAnimator = ScaleInAnimator()
    adapter = RepositoryAdapter(Glide.with(this), ArrayList())
    adapter.setOnClickListener { position, githubRepository -> onRepositoryClicked(position, githubRepository) }
    adapter.setOnLongClickListener { onRepositoryLongPressed(it) }
    recyclerView.adapter = AlphaInAnimationAdapter(adapter)
  }

  private fun setupObservers() {
    viewModel.getAddRepositoryButtonClicked.observe(this, Observer {
      activity?.startActivity(Intent(activity, RepositorySearchActivity::class.java))
    })
    viewModel.getRepositories.observe(this, Observer {
      repositories -> repositories?.let { adapter.updateData(it)
      swipeRefreshLayout.isRefreshing = false
    } })
    viewModel.getRepositoriesRemoved.observe(this, Observer { repositories ->
      repositories?.let {
        val isListEmpty = adapter.removeItems(repositories)
        if (isListEmpty) viewModel.emptiedList()
      }
    })
    viewModel.getRepositoryLoadError.observe(this, Observer {
      showErrorDialog(R.string.repository_loading_error)
      swipeRefreshLayout.isRefreshing = false
    })
    viewModel.getRepositoryDeleteError.observe(this, Observer {
      showErrorDialog(R.string.repository_delete_error)
      swipeRefreshLayout.isRefreshing = false
    })
  }

  private fun setupSwipeRefresh() {
    swipeRefreshLayout = binding.swipeRefreshLayout
    swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark)
    swipeRefreshLayout.setOnRefreshListener { viewModel.loadRepositories() }
  }

  private fun onRepositoryClicked(position: Int, githubRepository: GithubRepository) {
    if (adapter.getSelectedItemCount() > 0) {
      select(position)
    } else {
      activity?.let { startActivityForResult(RepositoryDetailActivity.newInstance(it, false, githubRepository), 1) }
    }
  }

  private fun onRepositoryLongPressed(position: Int) {
    if (actionMode == null) {
      actionMode = (activity as AppCompatActivity).startSupportActionMode(ActionModeCallback())
    }
    select(position)
  }

  private fun select(position: Int) {
    adapter.select(position)
    val count = adapter.getSelectedItemCount()
    when (count) {
      0 -> {
        actionMode?.finish()
        actionMode = null
      }
      else -> {
        actionMode?.title = count.toString()
        actionMode?.invalidate()
      }
    }
  }

  private fun deleteSelected() {
    viewModel.deleteRepositories(adapter.getSelectedItems())
    adapter.clearSelections()
    actionMode?.finish()
    actionMode = null
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    if (requestCode == 1) {
      if (resultCode == Activity.RESULT_OK) {
        val result = data!!.getStringExtra("result")
        if (getString(R.string.remove) == result) {
          showShortSnackbar(R.string.repository_removed_confirmation)
        }
      }
    }
  }

  inner class ActionModeCallback : ActionMode.Callback {
    override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
      mode.menuInflater.inflate(R.menu.repository_action_menu, menu)
      return true
    }

    override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
      return false
    }

    override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
      when(item.itemId) {
        R.id.action_delete -> {
          deleteSelected()
          return true
        }
      }
      return false
    }

    override fun onDestroyActionMode(mode: ActionMode) {
      adapter.clearSelections()
      actionMode = null
    }
  }
}
