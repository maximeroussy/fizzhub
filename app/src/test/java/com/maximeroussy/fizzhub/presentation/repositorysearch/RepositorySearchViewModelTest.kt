package com.maximeroussy.fizzhub.presentation.repositorysearch

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import com.maximeroussy.fizzhub.domain.GithubDataRepository
import com.maximeroussy.fizzhub.domain.models.GithubRepository
import com.maximeroussy.fizzhub.presentation.RxSchedulerRule
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class RepositorySearchViewModelTest {
  @get:Rule val rule: TestRule = InstantTaskExecutorRule()
  @get:Rule val schedulerRule = RxSchedulerRule()
  @MockK private lateinit var githubDataRepository: GithubDataRepository

  private lateinit var sut: RepositorySearchViewModel

  @Before
  fun setUp() {
    MockKAnnotations.init(this)
    sut = RepositorySearchViewModel(githubDataRepository)
  }

  @Test
  fun searchRepositories_callsSearchRepositoriesUseCase() {
    val searchQuery = "query"
    val repositoryList = ArrayList<GithubRepository>()
    every { githubDataRepository.searchRepositories(any()) } returns Single.just(repositoryList)

    sut.searchRepositories(searchQuery)

    verify { githubDataRepository.searchRepositories(eq(searchQuery)) }
  }

  @Test
  fun searchRepositories_updatesSearchResultsWhenSuccessful() {
    val searchQuery = "query"
    val repositoryList = ArrayList<GithubRepository>()
    val observer: Observer<List<GithubRepository>> = mockk(relaxed = true)
    every { githubDataRepository.searchRepositories(any()) } returns Single.just(repositoryList)
    sut.getSearchResults.observeForever(observer)

    sut.searchRepositories(searchQuery)

    verify { observer.onChanged(eq(repositoryList)) }
  }
}