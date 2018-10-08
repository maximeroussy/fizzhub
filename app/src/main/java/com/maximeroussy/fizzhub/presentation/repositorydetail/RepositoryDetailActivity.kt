package com.maximeroussy.fizzhub.presentation.repositorydetail

import android.annotation.SuppressLint
import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebView
import android.webkit.WebViewClient
import com.maximeroussy.fizzhub.MainApplication
import com.maximeroussy.fizzhub.R
import com.maximeroussy.fizzhub.domain.models.GithubRepository
import com.maximeroussy.fizzhub.presentation.BaseActivity
import com.maximeroussy.fizzhub.util.ViewModelFactory
import javax.inject.Inject

class RepositoryDetailActivity : BaseActivity() {
  @Inject internal lateinit var viewModelFactory: ViewModelFactory<RepositoryDetailViewModel>
  private lateinit var viewModel: RepositoryDetailViewModel
  private lateinit var webView: WebView

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    (application as MainApplication).component.inject(this)
    setContentView(R.layout.activity_github_webview_detail)
    setupToolbar()
    viewModel = ViewModelProviders.of(this, viewModelFactory).get(RepositoryDetailViewModel::class.java)
    setupObservers()
    setupWebContent()
  }

  private fun setupToolbar() {
    val toolbar: Toolbar = findViewById(R.id.toolbar)
    setSupportActionBar(toolbar)
    supportActionBar?.let {
      it.setDisplayShowHomeEnabled(true)
      it.setDisplayHomeAsUpEnabled(true)
      it.setHomeAsUpIndicator(R.drawable.ic_close)
      it.title = ""
    }
  }

  private fun setupObservers() {
    viewModel.getRepositorySaved.observe(this, Observer {
      finishWithResults(Activity.RESULT_OK, getString(R.string.save)
    ) })
    viewModel.getRepositoryRemoved.observe(this, Observer {
      finishWithResults(Activity.RESULT_OK, getString(R.string.remove))
    })
    viewModel.getRepositorySaveError.observe(this, Observer { showErrorDialog(R.string.repository_saving_error) })
    viewModel.getRepositoryDeleteError.observe(this, Observer { showErrorDialog(R.string.repository_delete_error) })
  }

  private fun finishWithResults(resultCode: Int, value: String) {
    val returnIntent = Intent()
    if (value.isNotEmpty()) {
      returnIntent.putExtra("result", value)
    }
    setResult(resultCode, returnIntent)
    finish()
  }

  private fun getGithubRepository(): GithubRepository {
    return intent.getSerializableExtra(GITHUB_REPOSITORY) as GithubRepository
  }

  @SuppressLint("SetJavaScriptEnabled")
  private fun setupWebContent() {
    val githubRepository = getGithubRepository()
    webView = findViewById(R.id.webview)
    webView.webViewClient = WebViewClient()
    webView.settings.javaScriptEnabled = true
    webView.settings.domStorageEnabled = true
    webView.loadUrl(githubRepository.repoUrl)
    viewModel.initialize(githubRepository)
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    val inflater = menuInflater
    if (intent.getBooleanExtra(FROM_SEARCH, false) && !getGithubRepository().tracked) {
      inflater.inflate(R.menu.menu_repository_detail_search, menu)
    } else {
      inflater.inflate(R.menu.menu_repository_detail, menu)
    }
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when(item.itemId) {
      android.R.id.home -> { finishWithResults(Activity.RESULT_CANCELED, "") }
      R.id.action_save -> viewModel.onSaveClicked()
      R.id.action_remove -> viewModel.onRemovedClicked()
    }
    return super.onOptionsItemSelected(item)
  }

  override fun onBackPressed() {
    if (webView.canGoBack()) {
      webView.goBack()
    } else {
      setResult(Activity.RESULT_CANCELED, Intent())
      super.onBackPressed()
    }
  }

  companion object {
    private const val GITHUB_REPOSITORY = "github_repository"
    private const val FROM_SEARCH = "from_search"
    fun newInstance(context: Context, fromSearch: Boolean, githubRepository: GithubRepository): Intent {
      val intent = Intent(context, RepositoryDetailActivity::class.java)
      intent.putExtra(GITHUB_REPOSITORY, githubRepository)
      intent.putExtra(FROM_SEARCH, fromSearch)
      return intent
    }
  }
}
