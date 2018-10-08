package com.maximeroussy.fizzhub.presentation.issuelist

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.view.MenuItemCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.maximeroussy.fizzhub.MainApplication
import com.maximeroussy.fizzhub.R
import com.maximeroussy.fizzhub.databinding.FragmentIssueListBinding
import com.maximeroussy.fizzhub.domain.models.GithubIssue
import com.maximeroussy.fizzhub.presentation.BaseFragment
import com.maximeroussy.fizzhub.presentation.issuedetail.IssueDetailActivity
import com.maximeroussy.fizzhub.util.ViewModelFactory
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter
import jp.wasabeef.recyclerview.animators.ScaleInAnimator
import javax.inject.Inject

class IssueListFragment : BaseFragment<FragmentIssueListBinding>(R.layout.fragment_issue_list), SearchView.OnQueryTextListener {
  @Inject internal lateinit var viewModelFactory: ViewModelFactory<IssueListViewModel>
  private lateinit var viewModel: IssueListViewModel
  private lateinit var adapter: IssueAdapter
  private lateinit var swipeRefreshLayout: SwipeRefreshLayout

  private var isLoading = false
  private var currentQuery = ""

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    viewModel = ViewModelProviders.of(this, viewModelFactory).get(IssueListViewModel::class.java)
    binding.viewModel = viewModel
    setupToolbar()
    setupObservers()
    setupRecyclerView()
    setupSwipeRefresh()
    viewModel.loadIssues()
  }

  override fun onAttach(context: Context?) {
    (context?.applicationContext as MainApplication).component.inject(this)
    super.onAttach(context)
  }

  private fun setupToolbar() {
    val toolbar = binding.toolbar
    toolbar.inflateMenu(R.menu.menu_issue_list)
    val menu = toolbar.menu
    val searchItem = menu.findItem(R.id.action_search)
    val searchView = MenuItemCompat.getActionView(searchItem) as SearchView
    searchView.setOnQueryTextListener(this)
  }

  private fun setupObservers() {
    viewModel.getIssueResults.observe(this, Observer { githubIssues -> githubIssues?.let {
      adapter.updateData(it)
      stopLoading()
    } })
    viewModel.getNextPageIssueResults.observe(this, Observer { githubIssues -> githubIssues?.let {
      adapter.addData(githubIssues)
      stopLoading()
    } })
    viewModel.getIssueLoadError.observe(this, Observer {
      showErrorDialog(R.string.issue_loading_error)
      stopLoading()
    })
    viewModel.getIssueSearchError.observe(this, Observer {
      showErrorDialog(R.string.issue_search_error)
      stopLoading()
    })
  }

  private fun stopLoading() {
    isLoading = false
    swipeRefreshLayout.isRefreshing = false
  }

  private fun setupRecyclerView() {
    val recyclerView = binding.recyclerView
    val layoutManager = LinearLayoutManager(activity)
    recyclerView.layoutManager = layoutManager
    recyclerView.itemAnimator = ScaleInAnimator()
    adapter = IssueAdapter(ArrayList())
    adapter.setOnClickListener { githubIssue -> onIssueClicked(githubIssue) }
    recyclerView.adapter = AlphaInAnimationAdapter(adapter)
    recyclerView.addOnScrollListener(object: RecyclerView.OnScrollListener() {
      override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        if (isLoading) return
        val visibleItemCount = layoutManager.childCount
        val totalItemCount = layoutManager.itemCount
        val pastVisibleItems = layoutManager.findFirstVisibleItemPosition()
        if (pastVisibleItems + visibleItemCount >= totalItemCount) {
          if (currentQuery.isNotBlank()) {
            viewModel.searchMoreIssues(currentQuery)
          } else {
            viewModel.loadMoreIssues()
          }
          isLoading = true
        }
      }
    })
  }

  private fun setupSwipeRefresh() {
    swipeRefreshLayout = binding.swipeRefreshLayout
    swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark)
    swipeRefreshLayout.setOnRefreshListener {
      if (currentQuery.isNotBlank()) {
        viewModel.searchIssues(currentQuery)
      } else {
        viewModel.loadIssues()
      }
    }
  }

  private fun onIssueClicked(githubIssue: GithubIssue) {
    activity?.let { startActivity(IssueDetailActivity.newInstance(it, githubIssue)) }
  }

  override fun onQueryTextSubmit(query: String): Boolean {
    if (query.isNotEmpty()) {
      currentQuery = query
      viewModel.searchIssues(query)
      activity?.let {
        val inputManager = it.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(it.currentFocus!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
      }
    }
    return true
  }

  override fun onQueryTextChange(query: String): Boolean {
    if (query.isEmpty()) {
      viewModel.loadIssues()
      currentQuery = ""
    }
    return true
  }
}
