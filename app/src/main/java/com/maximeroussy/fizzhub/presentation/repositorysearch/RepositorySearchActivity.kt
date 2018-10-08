package com.maximeroussy.fizzhub.presentation.repositorysearch

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import com.bumptech.glide.Glide
import com.maximeroussy.fizzhub.MainApplication
import com.maximeroussy.fizzhub.R
import com.maximeroussy.fizzhub.databinding.ActivityRepositorySearchBinding
import com.maximeroussy.fizzhub.domain.models.GithubRepository
import com.maximeroussy.fizzhub.presentation.BaseActivity
import com.maximeroussy.fizzhub.presentation.repositorydetail.RepositoryDetailActivity
import com.maximeroussy.fizzhub.presentation.repositorylist.RepositoryAdapter
import com.maximeroussy.fizzhub.util.ViewModelFactory
import javax.inject.Inject

class RepositorySearchActivity : BaseActivity() {
  private lateinit var binding: ActivityRepositorySearchBinding
  @Inject internal lateinit var viewModelFactory: ViewModelFactory<RepositorySearchViewModel>
  private lateinit var viewModel: RepositorySearchViewModel
  private lateinit var adapter: RepositoryAdapter
  private lateinit var swipeRefreshLayout: SwipeRefreshLayout
  private var isLoading = false

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    (application as MainApplication).component.inject(this)
    binding = DataBindingUtil.setContentView(this, R.layout.activity_repository_search)
    setupToolbar()
    viewModel = ViewModelProviders.of(this, viewModelFactory).get(RepositorySearchViewModel::class.java)
    setupSearchButton()
    setupRecyclerView()
    setupObservers()
    setupSwipeRefresh()
  }

  private fun setupToolbar() {
    val toolbar = binding.toolbar
    setSupportActionBar(toolbar)
    supportActionBar?.let {
      it.setDisplayShowHomeEnabled(true)
      it.setDisplayHomeAsUpEnabled(true)
      it.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
    }
  }

  private fun setupSearchButton() {
    binding.searchEditText.setOnEditorActionListener { _, actionId, _ ->
      if (actionId == EditorInfo.IME_ACTION_SEARCH) {
        swipeRefreshLayout.isRefreshing = true
        viewModel.searchRepositories(binding.searchEditText.text.toString())
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(currentFocus!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
      }
      true
    }
  }

  private fun setupRecyclerView() {
    val recyclerView = binding.recyclerView
    val layoutManager = LinearLayoutManager(this)
    recyclerView.layoutManager = layoutManager
    adapter = RepositoryAdapter(Glide.with(this), ArrayList())
    adapter.setOnClickListener { _, githubRepository -> onRepositoryClicked(githubRepository) }
    recyclerView.adapter = adapter
    recyclerView.addOnScrollListener(object: RecyclerView.OnScrollListener() {
      override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        if (isLoading) return
        val visibleItemCount = layoutManager.childCount
        val totalItemCount = layoutManager.itemCount
        val pastVisibleItems = layoutManager.findFirstVisibleItemPosition()
        if (pastVisibleItems + visibleItemCount >= totalItemCount) {
          viewModel.loadMore(binding.searchEditText.text.toString())
          isLoading = true
        }
      }
    })
  }

  private fun setupObservers() {
    viewModel.getSearchResults.observe(this, Observer { repositories ->
      repositories?.let {
        adapter.updateData(it)
        stopLoading()
      }
    })
    viewModel.getNextPageResults.observe(this, Observer { repositories ->
      repositories?.let {
        adapter.addData(it)
        stopLoading()
      }
    })
    viewModel.getRepositorySearchError.observe(this, Observer {
      showErrorDialog(R.string.repository_search_error)
      stopLoading()
    })
  }

  private fun stopLoading() {
    isLoading = false
    swipeRefreshLayout.isRefreshing = false
  }

  private fun setupSwipeRefresh() {
    swipeRefreshLayout = binding.swipeRefreshLayout
    swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark)
    swipeRefreshLayout.setOnRefreshListener { viewModel.searchRepositories(binding.searchEditText.text.toString()) }
  }

  private fun onRepositoryClicked(githubRepository: GithubRepository) {
    startActivityForResult(RepositoryDetailActivity.newInstance(this, true, githubRepository), 1)
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    if (requestCode == 1) {
      if (resultCode == Activity.RESULT_OK) {
        val result = data!!.getStringExtra("result")
        if (getString(R.string.save) == result) {
          showShortSnackbar(R.string.repository_saved_confirmation)
        } else if (getString(R.string.remove) == result) {
          showShortSnackbar(R.string.repository_removed_confirmation)
          swipeRefreshLayout.isRefreshing = true
          viewModel.searchRepositories(binding.searchEditText.text.toString())
        }
      }
    }
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when(item.itemId) {
      android.R.id.home -> onBackPressed()
    }
    return super.onOptionsItemSelected(item)
  }
}
