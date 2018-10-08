package com.maximeroussy.fizzhub.presentation.repositorydetail

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
import io.reactivex.Completable
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class RepositoryDetailViewModelTest {
  @get:Rule val rule: TestRule = InstantTaskExecutorRule()
  @get:Rule val schedulerRule = RxSchedulerRule()
  @MockK private lateinit var githubDataRepository: GithubDataRepository

  private lateinit var sut: RepositoryDetailViewModel

  @Before
  fun setUp() {
    MockKAnnotations.init(this)
    sut = RepositoryDetailViewModel(githubDataRepository)
  }

  @Test
  fun onSaveClicked_callsGithubDataRepository() {
    val githubRepository: GithubRepository = mockk()
    every { githubDataRepository.saveRepository(any()) } returns Completable.complete()
    sut.initialize(githubRepository)

    sut.onSaveClicked()

    verify { githubDataRepository.saveRepository(eq(githubRepository)) }
  }

  @Test
  fun onSaveClicked_callsRepositorySavedEventWhenSuccessful() {
    val githubRepository: GithubRepository = mockk()
    val observer: Observer<Any> = mockk(relaxed = true)
    every { githubDataRepository.saveRepository(any()) } returns Completable.complete()
    sut.initialize(githubRepository)
    sut.getRepositorySaved.observeForever(observer)

    sut.onSaveClicked()

    verify { observer.onChanged(null) }
  }

  @Test
  fun onRemovedClicked_callsGithubDataRepository() {
    val githubRepository: GithubRepository = mockk()
    every { githubDataRepository.deleteRepository(any()) } returns Completable.complete()
    sut.initialize(githubRepository)

    sut.onRemovedClicked()

    verify { githubDataRepository.deleteRepository(eq(githubRepository)) }
  }

  @Test
  fun onRemovedClicked_callsRepositoryRemovedEventWhenSuccessful() {
    val githubRepository: GithubRepository = mockk()
    val observer: Observer<Any> = mockk(relaxed = true)
    every { githubDataRepository.deleteRepository(any()) } returns Completable.complete()
    sut.initialize(githubRepository)
    sut.getRepositoryRemoved.observeForever(observer)

    sut.onRemovedClicked()

    verify { observer.onChanged(null) }
  }
}