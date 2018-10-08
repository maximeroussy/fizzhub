package com.maximeroussy.fizzhub.presentation.issuelist

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import com.maximeroussy.fizzhub.domain.models.GithubIssue
import com.maximeroussy.fizzhub.domain.usecases.GetAllIssues
import com.maximeroussy.fizzhub.domain.usecases.SearchIssues
import com.maximeroussy.fizzhub.util.SingleLiveEvent
import com.maximeroussy.fizzhub.util.addTo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class IssueListViewModel @Inject constructor(
    private val getAllIssues: GetAllIssues,
    private val searchIssues: SearchIssues
): ViewModel() {

  val isListEmpty = ObservableField<Boolean>(false)

  private val compositeDisposable = CompositeDisposable()
  private val issueResults = MutableLiveData<List<GithubIssue>>()
  private val nextPageIssueResults = MutableLiveData<List<GithubIssue>>()
  private val issueLoadError = SingleLiveEvent<Any>()
  private val issueSearchError = SingleLiveEvent<Any>()

  val getIssueResults: LiveData<List<GithubIssue>>
    get() = issueResults

  val getNextPageIssueResults: LiveData<List<GithubIssue>>
    get() = nextPageIssueResults

  val getIssueLoadError: LiveData<Any>
    get() = issueLoadError

  val getIssueSearchError: LiveData<Any>
    get() = issueSearchError

  fun loadIssues() {
    getAllIssues.first()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({
          result -> issueResults.value = result
          isListEmpty.set(result.isEmpty())
        }, { issueLoadError.call() })
        .addTo(compositeDisposable)
  }

  fun loadMoreIssues() {
    getAllIssues.next()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({
          result -> nextPageIssueResults.value = result
        }, { issueLoadError.call() })
        .addTo(compositeDisposable)
  }

  fun searchIssues(query: String) {
    searchIssues.first(query)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({
          result -> issueResults.value = result
          isListEmpty.set(result.isEmpty())
        }, { issueSearchError.call() })
        .addTo(compositeDisposable)
  }

  fun searchMoreIssues(query: String) {
    searchIssues.next(query)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({
          result -> nextPageIssueResults.value = result
        }, { issueSearchError.call() })
        .addTo(compositeDisposable)
  }

  override fun onCleared() {
    compositeDisposable.clear()
    super.onCleared()
  }
}
