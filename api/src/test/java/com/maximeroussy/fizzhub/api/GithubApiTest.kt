package com.maximeroussy.fizzhub.api

import com.maximeroussy.fizzhub.api.models.GithubIssueResponse
import com.maximeroussy.fizzhub.api.models.GithubIssueSearchResponse
import com.maximeroussy.fizzhub.api.models.GithubRepositoryResponse
import com.maximeroussy.fizzhub.api.models.GithubRepositorySearchResponse
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class GithubApiTest {
  @MockK private lateinit var githubEndpoint: GithubEndpoint
  @MockK private lateinit var githubHeaderParser: GithubHeaderParser

  private lateinit var sut: GithubApi

  @Before
  fun setUp() {
    MockKAnnotations.init(this)
    sut = GithubApi(githubEndpoint, githubHeaderParser)
  }

  @Test
  fun searchRepositories_callsGithubSearchRepositoriesEndpoint() {
    val searchQuery = "query"
    val page = 0
    val repositorySearchResponse: GithubRepositorySearchResponse = mockk()
    val apiResponse = Response.success(repositorySearchResponse)
    every { githubEndpoint.searchRepositories(any(), any()) } returns Single.just(apiResponse)
    every { githubHeaderParser.getNextPage(any()) } returns 2

    sut.searchRepositories(searchQuery, page).test()

    verify { githubEndpoint.searchRepositories(eq(searchQuery), eq(page)) }
  }

  @Test
  fun searchRepositories_callsGithubHeaderParserWhenResponseIsSuccessful() {
    val searchQuery = "query"
    val page = 0
    val repositorySearchResponse: GithubRepositorySearchResponse = mockk()
    val apiResponse = Response.success(repositorySearchResponse)
    every { githubEndpoint.searchRepositories(any(), any()) } returns Single.just(apiResponse)
    every { githubHeaderParser.getNextPage(any()) } returns 2

    sut.searchRepositories(searchQuery, page).test()

    verify { githubHeaderParser.getNextPage(any()) }
  }

  @Test
  fun searchRepositories_returnsSinglePagedGithubSearchRepositoryResponseWithCorrectListWhenResponseIsSuccessful() {
    val searchQuery = "query"
    val page = 0
    val repositoryResponseList = ArrayList<GithubRepositoryResponse>()
    val repositorySearchResponse = GithubRepositorySearchResponse(0, false, repositoryResponseList)
    val apiResponse = Response.success(repositorySearchResponse)
    every { githubEndpoint.searchRepositories(any(), any()) } returns Single.just(apiResponse)
    every { githubHeaderParser.getNextPage(any()) } returns 2

    val testObserver = sut.searchRepositories(searchQuery, page).test()

    testObserver.assertValue { it.items == repositoryResponseList }
  }

  @Test
  fun searchRepositories_returnsSinglePagedGithubSearchRepositoryResponseWithCorrectNextPageWhenResponseIsSuccessful() {
    val searchQuery = "query"
    val page = 0
    val nextPage = 2
    val repositoryResponseList = ArrayList<GithubRepositoryResponse>()
    val repositorySearchResponse = GithubRepositorySearchResponse(0, false, repositoryResponseList)
    val apiResponse = Response.success(repositorySearchResponse)
    every { githubEndpoint.searchRepositories(any(), any()) } returns Single.just(apiResponse)
    every { githubHeaderParser.getNextPage(any()) } returns nextPage

    val testObserver = sut.searchRepositories(searchQuery, page).test()

    testObserver.assertValue { it.nextPage == nextPage }
  }

  @Test
  fun searchRepositories_returnsSingleErrorWhenResponseIsNotSuccessful() {
    val searchQuery = "query"
    val page = 0
    val response = Response.error<GithubRepositorySearchResponse>(404, mockk())
    every { githubEndpoint.searchRepositories(any(), any()) } returns Single.just(response)

    val testObserver = sut.searchRepositories(searchQuery, page).test()

    testObserver.assertError(Throwable::class.java)
  }

  @Test
  fun getIssues_callsGithubIssuesEndpoint() {
    val userName = "user_name"
    val repoName = "repo_name"
    val page = 0
    val githubIssueResponse: List<GithubIssueResponse> = ArrayList()
    val apiResponse = Response.success(githubIssueResponse)
    every { githubEndpoint.getIssues(any(), any(), any()) } returns Single.just(apiResponse)
    every { githubHeaderParser.getNextPage(any()) } returns 2

    sut.getIssues(userName, repoName, page).test()

    verify { githubEndpoint.getIssues(eq(userName), eq(repoName), eq(page)) }
  }

  @Test
  fun getIssues_callsGithubHeaderParserWhenResponseIsSuccessful() {
    val userName = "user_name"
    val repoName = "repo_name"
    val page = 0
    val githubIssueResponse: List<GithubIssueResponse> = ArrayList()
    val apiResponse = Response.success(githubIssueResponse)
    every { githubEndpoint.getIssues(any(), any(), any()) } returns Single.just(apiResponse)
    every { githubHeaderParser.getNextPage(any()) } returns 2

    sut.getIssues(userName, repoName, page).test()

    verify { githubHeaderParser.getNextPage(any()) }
  }

  @Test
  fun getIssues_returnsSinglePagedGithubIssueResponseWithCorrectListWhenResponseIsSuccessful() {
    val userName = "user_name"
    val repoName = "repo_name"
    val page = 0
    val githubIssueResponse: List<GithubIssueResponse> = ArrayList()
    val apiResponse = Response.success(githubIssueResponse)
    every { githubEndpoint.getIssues(any(), any(), any()) } returns Single.just(apiResponse)
    every { githubHeaderParser.getNextPage(any()) } returns 2

    val testObserver = sut.getIssues(userName, repoName, page).test()

    testObserver.assertValue { it.items == githubIssueResponse }
  }

  @Test
  fun getIssues_returnsSinglePagedGithubIssueResponseWithCorrectNextPageWhenResponseIsSuccessful() {
    val userName = "user_name"
    val repoName = "repo_name"
    val page = 0
    val nextPage = 2
    val githubIssueResponse: List<GithubIssueResponse> = ArrayList()
    val apiResponse = Response.success(githubIssueResponse)
    every { githubEndpoint.getIssues(any(), any(), any()) } returns Single.just(apiResponse)
    every { githubHeaderParser.getNextPage(any()) } returns nextPage

    val testObserver = sut.getIssues(userName, repoName, page).test()

    testObserver.assertValue { it.nextPage == nextPage }
  }

  @Test
  fun getIssues_returnsSingleErrorWhenResponseIsNotSuccessful() {
    val userName = "user_name"
    val repoName = "repo_name"
    val page = 0
    val apiResponse = Response.error<List<GithubIssueResponse>>(404, mockk())
    every { githubEndpoint.getIssues(any(), any(), any()) } returns Single.just(apiResponse)
    every { githubHeaderParser.getNextPage(any()) } returns 2

    val testObserver = sut.getIssues(userName, repoName, page).test()

    testObserver.assertError(Throwable::class.java)
  }

  @Test
  fun searchIssues_callsGithubSearchIssuesEndpoint() {
    val query = "query"
    val repoFullName = "user_name/repo_name"
    val page = 0
    val githubIssueSearchResponse: GithubIssueSearchResponse = mockk()
    val apiResponse = Response.success(githubIssueSearchResponse)
    every { githubEndpoint.searchIssues(any(), any()) } returns Single.just(apiResponse)
    every { githubHeaderParser.getNextPage(any()) } returns 2

    sut.searchIssues(query, repoFullName, page).test()

    verify { githubEndpoint.searchIssues(eq("$query+$repoFullName"), eq(page)) }
  }

  @Test
  fun searchIssues_callsGithubHeaderParserWhenResponseIsSuccessful() {
    val query = "query"
    val repoFullName = "user_name/repo_name"
    val page = 0
    val githubIssueSearchResponse: GithubIssueSearchResponse = mockk()
    val apiResponse = Response.success(githubIssueSearchResponse)
    every { githubEndpoint.searchIssues(any(), any()) } returns Single.just(apiResponse)
    every { githubHeaderParser.getNextPage(any()) } returns 2

    sut.searchIssues(query, repoFullName, page).test()

    verify { githubHeaderParser.getNextPage(any()) }
  }

  @Test
  fun searchIssues_returnsSinglePagedGithubIssueResponseWithCorrectListWhenResponseIsSuccessful() {
    val query = "query"
    val repoFullName = "user_name/repo_name"
    val page = 0
    val githubIssueResponseList: List<GithubIssueResponse> = ArrayList()
    val githubIssueSearchResponse = GithubIssueSearchResponse(0, false, githubIssueResponseList)
    val apiResponse = Response.success(githubIssueSearchResponse)
    every { githubEndpoint.searchIssues(any(), any()) } returns Single.just(apiResponse)
    every { githubHeaderParser.getNextPage(any()) } returns 2

    val testObserver = sut.searchIssues(query, repoFullName, page).test()

    testObserver.assertValue { it.items == githubIssueResponseList }
  }

  @Test
  fun searchIssues_returnsSinglePagedGithubIssueResponseWithCorrectNextPageWhenResponseIsSuccessful() {
    val query = "query"
    val repoFullName = "user_name/repo_name"
    val page = 0
    val nextPage = 2
    val githubIssueResponseList: List<GithubIssueResponse> = ArrayList()
    val githubIssueSearchResponse = GithubIssueSearchResponse(0, false, githubIssueResponseList)
    val apiResponse = Response.success(githubIssueSearchResponse)
    every { githubEndpoint.searchIssues(any(), any()) } returns Single.just(apiResponse)
    every { githubHeaderParser.getNextPage(any()) } returns nextPage

    val testObserver = sut.searchIssues(query, repoFullName, page).test()

    testObserver.assertValue { it.nextPage == nextPage }
  }

  @Test
  fun searchIssues_returnsSingleErrorWhenResponseIsNotSuccessful() {
    val query = "query"
    val repoFullName = "user_name/repo_name"
    val page = 0
    val apiResponse = Response.error<GithubIssueSearchResponse>(404, mockk())
    every { githubEndpoint.searchIssues(any(), any()) } returns Single.just(apiResponse)
    every { githubHeaderParser.getNextPage(any()) } returns 2

    val testObserver = sut.searchIssues(query, repoFullName, page).test()

    testObserver.assertError(Throwable::class.java)
  }
}