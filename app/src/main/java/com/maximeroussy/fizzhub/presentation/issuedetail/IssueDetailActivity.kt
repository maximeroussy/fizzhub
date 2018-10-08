package com.maximeroussy.fizzhub.presentation.issuedetail

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.webkit.WebView
import android.webkit.WebViewClient
import com.maximeroussy.fizzhub.R
import com.maximeroussy.fizzhub.domain.models.GithubIssue

class IssueDetailActivity : AppCompatActivity() {
  private lateinit var webView: WebView

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_github_webview_detail)
    setupToolbar()
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

  @SuppressLint("SetJavaScriptEnabled")
  private fun setupWebContent() {
    val githubIssue = intent.getSerializableExtra(GITHUB_ISSUE) as GithubIssue
    webView = findViewById(R.id.webview)
    webView.webViewClient = WebViewClient()
    webView.settings.javaScriptEnabled = true
    webView.settings.domStorageEnabled = true
    webView.loadUrl(githubIssue.issueUrl)
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when(item.itemId) {
      android.R.id.home -> finish()
    }
    return super.onOptionsItemSelected(item)
  }

  override fun onBackPressed() {
    if (webView.canGoBack()) {
      webView.goBack()
    } else {
      super.onBackPressed()
    }
  }

  companion object {
    private const val GITHUB_ISSUE = "github_issue"
    fun newInstance(context: Context, githubIssue: GithubIssue): Intent {
      val intent = Intent(context, IssueDetailActivity::class.java)
      intent.putExtra(GITHUB_ISSUE, githubIssue)
      return intent
    }
  }
}
