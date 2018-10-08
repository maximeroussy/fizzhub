package com.maximeroussy.fizzhub.presentation.repositorysearch

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import com.maximeroussy.fizzhub.domain.GithubDataRepository
import com.maximeroussy.fizzhub.domain.models.GithubRepository
import com.maximeroussy.fizzhub.util.SingleLiveEvent
import com.maximeroussy.fizzhub.util.addTo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class RepositorySearchViewModel @Inject constructor(private val githubDataRepository: GithubDataRepository) : ViewModel() {
  val isListEmpty = ObservableField<Boolean>(true)

  private val compositeDisposable = CompositeDisposable()
  private val searchResults = MutableLiveData<List<GithubRepository>>()
  private val nextPageResults = MutableLiveData<List<GithubRepository>>()
  private val repositorySearchError = SingleLiveEvent<Any>()

  val getSearchResults: LiveData<List<GithubRepository>>
    get() = searchResults

  val getNextPageResults: LiveData<List<GithubRepository>>
    get() = nextPageResults

  val getRepositorySearchError: LiveData<Any>
    get() = repositorySearchError

  fun searchRepositories(query: String) {
    githubDataRepository.searchRepositories(query)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({
          result -> searchResults.value = result
          isListEmpty.set(result.isEmpty())
        }, { repositorySearchError.call() })
        .addTo(compositeDisposable)
  }

  fun loadMore(query: String) {
    githubDataRepository.loadMoreSearchRepositories(query)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({ result -> nextPageResults.value = result }, { repositorySearchError.call() })
        .addTo(compositeDisposable)
  }

  override fun onCleared() {
    compositeDisposable.clear()
    super.onCleared()
  }
}
