package com.maximeroussy.fizzhub.presentation.repositorydetail

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.maximeroussy.fizzhub.domain.GithubDataRepository
import com.maximeroussy.fizzhub.domain.models.GithubRepository
import com.maximeroussy.fizzhub.util.SingleLiveEvent
import com.maximeroussy.fizzhub.util.addTo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class RepositoryDetailViewModel @Inject constructor(
    private val githubDataRepository: GithubDataRepository
) : ViewModel() {

  private lateinit var githubRepository: GithubRepository
  private val compositeDisposable = CompositeDisposable()
  private val repositorySaved = SingleLiveEvent<Any>()
  private val repositoryRemoved = SingleLiveEvent<Any>()
  private val repositorySaveError = SingleLiveEvent<Any>()
  private val repositoryDeleteError = SingleLiveEvent<Any>()

  val getRepositorySaved: LiveData<Any>
    get() = repositorySaved

  val getRepositoryRemoved: LiveData<Any>
    get() = repositoryRemoved

  val getRepositorySaveError: LiveData<Any>
    get() = repositorySaveError

  val getRepositoryDeleteError: LiveData<Any>
    get() = repositoryDeleteError

  fun initialize(githubRepository: GithubRepository) {
    this.githubRepository = githubRepository
  }

  fun onSaveClicked() {
    githubDataRepository.saveRepository(githubRepository)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({ repositorySaved.call() }, { repositorySaveError.call() })
        .addTo(compositeDisposable)
  }

  fun onRemovedClicked() {
    githubDataRepository.deleteRepository(githubRepository)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({ repositoryRemoved.call() }, { repositoryDeleteError.call() })
        .addTo(compositeDisposable)
  }

  override fun onCleared() {
    compositeDisposable.clear()
    super.onCleared()
  }
}
