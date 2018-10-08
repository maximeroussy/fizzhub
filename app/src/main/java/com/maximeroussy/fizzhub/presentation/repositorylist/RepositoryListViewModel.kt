package com.maximeroussy.fizzhub.presentation.repositorylist

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import com.maximeroussy.fizzhub.domain.GithubDataRepository
import com.maximeroussy.fizzhub.domain.models.GithubRepository
import com.maximeroussy.fizzhub.util.SingleLiveEvent
import com.maximeroussy.fizzhub.util.addTo
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class RepositoryListViewModel @Inject constructor(
    private val githubDataRepository: GithubDataRepository
) : ViewModel() {

  val isListEmpty = ObservableField<Boolean>(true)

  private val compositeDisposable = CompositeDisposable()
  private val addRepositoryButtonClicked = SingleLiveEvent<Any>()
  private val repositoryLoadError = SingleLiveEvent<Any>()
  private val repositoryDeleteError = SingleLiveEvent<Any>()
  private val repositories = MutableLiveData<List<GithubRepository>>()
  private val repositoriesRemoved = MutableLiveData<List<GithubRepository>>()

  val getAddRepositoryButtonClicked: LiveData<Any>
    get() = addRepositoryButtonClicked

  val getRepositoryLoadError: LiveData<Any>
    get() = repositoryLoadError

  val getRepositoryDeleteError: LiveData<Any>
    get() = repositoryDeleteError

  val getRepositories: LiveData<List<GithubRepository>>
    get() = repositories

  val getRepositoriesRemoved: LiveData<List<GithubRepository>>
    get() = repositoriesRemoved

  fun loadRepositories() {
    githubDataRepository.getAllSavedRepositories()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({ repositoryList ->
          repositories.value = repositoryList
          isListEmpty.set(repositoryList.isEmpty())
        }, { repositoryLoadError.call() })
        .addTo(compositeDisposable)
  }

  fun deleteRepositories(githubRepositories: List<GithubRepository>) {
    Observable.just(githubRepositories).flatMapIterable { list -> list }
        .flatMapCompletable { githubDataRepository.deleteRepository(it) }
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({ repositoriesRemoved.value = githubRepositories }, { repositoryDeleteError.call() })
        .addTo(compositeDisposable)
  }

  fun onRefresh() {
    loadRepositories()
  }

  fun emptiedList() {
    isListEmpty.set(true)
  }

  fun addRepositoryButtonClicked() {
    addRepositoryButtonClicked.call()
  }

  override fun onCleared() {
    compositeDisposable.clear()
    super.onCleared()
  }
}
