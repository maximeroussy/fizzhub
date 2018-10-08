package com.maximeroussy.fizzhub.presentation.repositorylist

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import com.maximeroussy.fizzhub.domain.GithubDataRepository
import com.maximeroussy.fizzhub.domain.models.GithubRepository
import com.maximeroussy.fizzhub.presentation.RxSchedulerRule
import io.mockk.Called
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Completable
import io.reactivex.Single
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class RepositoryListViewModelTest {
  @get:Rule val rule: TestRule = InstantTaskExecutorRule()
  @get:Rule val schedulerRule = RxSchedulerRule()

  @MockK private lateinit var githubDataRepository: GithubDataRepository

  private lateinit var sut: RepositoryListViewModel

  @Before
  fun setUp() {
    MockKAnnotations.init(this)
    sut = RepositoryListViewModel(githubDataRepository)
  }

  @Test
  fun loadRepositories_callsGithubDataRepository() {
    val repositoryList = ArrayList<GithubRepository>()
    every { githubDataRepository.getAllSavedRepositories() } returns Single.just(repositoryList)

    sut.loadRepositories()

    verify { githubDataRepository.getAllSavedRepositories() }
  }

  @Test
  fun loadRepositories_updatesRepositoriesListWhenSuccessful() {
    val repositoryList = ArrayList<GithubRepository>()
    val observer: Observer<List<GithubRepository>> = mockk(relaxed = true)
    every { githubDataRepository.getAllSavedRepositories() } returns Single.just(repositoryList)
    sut.getRepositories.observeForever(observer)

    sut.loadRepositories()

    verify { observer.onChanged(eq(repositoryList)) }
  }

  @Test
  fun loadRepositories_setsEmptyListObservableToTrueIfListIsEmpty() {
    val repositoryList = ArrayList<GithubRepository>()
    every { githubDataRepository.getAllSavedRepositories() } returns Single.just(repositoryList)

    sut.loadRepositories()

    assertEquals(sut.isListEmpty.get(), true)
  }

  @Test
  fun loadRepositories_setsEmptyListObservableToFalseIfListIsNotEmpty() {
    val repositoryList = ArrayList<GithubRepository>()
    repositoryList.add(mockk())
    every { githubDataRepository.getAllSavedRepositories() } returns Single.just(repositoryList)

    sut.loadRepositories()

    assertEquals(sut.isListEmpty.get(), false)
  }

  @Test
  fun deleteRepositories_callsGithubDataRepositoryForAllValues() {
    val repository1: GithubRepository = mockk()
    val repository2: GithubRepository = mockk()
    val repository3: GithubRepository = mockk()
    val repositoryList = listOf(repository1, repository2, repository3)
    every { githubDataRepository.deleteRepository(any()) } returns Completable.complete()

    sut.deleteRepositories(repositoryList)

    verify { githubDataRepository.deleteRepository(eq(repository1)) }
    verify { githubDataRepository.deleteRepository(eq(repository2)) }
    verify { githubDataRepository.deleteRepository(eq(repository3)) }
  }

  @Test
  fun deleteRepositories_doesNotCallGithubDataRepositoryForEmptyList() {
    val repositoryList = ArrayList<GithubRepository>()
    every { githubDataRepository.deleteRepository(any()) } returns Completable.complete()

    sut.deleteRepositories(repositoryList)

    verify { githubDataRepository.deleteRepository(any()) wasNot Called }
  }

  @Test
  fun deleteRepositories_updatesRepositoriesRemovedListWhenSuccessful() {
    val repository1: GithubRepository = mockk()
    val repository2: GithubRepository = mockk()
    val repository3: GithubRepository = mockk()
    val repositoryList = listOf(repository1, repository2, repository3)
    val observer: Observer<List<GithubRepository>> = mockk(relaxed = true)
    every { githubDataRepository.deleteRepository(any()) } returns Completable.complete()
    sut.getRepositoriesRemoved.observeForever(observer)

    sut.deleteRepositories(repositoryList)

    verify { observer.onChanged(eq(repositoryList)) }
  }

  @Test
  fun emptiedList_setsEmptyListObservableToTrue() {
    sut.emptiedList()

    assertEquals(sut.isListEmpty.get(), true)
  }

  @Test
  fun addRepositoryButtonClicked_callsAddRepositoryButtonClickedSingleLiveEvent() {
    val observer: Observer<Any> = mockk(relaxed = true)
    sut.getAddRepositoryButtonClicked.observeForever(observer)

    sut.addRepositoryButtonClicked()

    verify { observer.onChanged(null) }
  }
}