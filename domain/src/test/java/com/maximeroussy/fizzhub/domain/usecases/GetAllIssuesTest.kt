package com.maximeroussy.fizzhub.domain.usecases

import com.maximeroussy.fizzhub.domain.GithubDataRepository
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import org.junit.Before

import org.junit.Assert.*

class GetAllIssuesTest {
  @MockK private lateinit var githubDataRepository: GithubDataRepository

  private lateinit var sut: GetAllIssues

  @Before
  fun setUp() {
    MockKAnnotations.init(this)
    sut = GetAllIssues(githubDataRepository)
  }
}