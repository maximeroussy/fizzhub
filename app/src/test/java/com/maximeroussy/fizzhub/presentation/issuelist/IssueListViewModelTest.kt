package com.maximeroussy.fizzhub.presentation.issuelist

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.maximeroussy.fizzhub.domain.usecases.GetAllIssues
import com.maximeroussy.fizzhub.domain.usecases.SearchIssues
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import org.junit.Before
import org.junit.Rule
import org.junit.rules.TestRule

class IssueListViewModelTest {
  @MockK private lateinit var getAllIssues: GetAllIssues
  @MockK private lateinit var searchIssues: SearchIssues

  @get:Rule val rule: TestRule = InstantTaskExecutorRule()
  private lateinit var sut: IssueListViewModel

  @Before
  fun setUp() {
    MockKAnnotations.init(this)
    sut = IssueListViewModel(getAllIssues, searchIssues)
  }
}