package com.maximeroussy.fizzhub.api

import com.maximeroussy.fizzhub.api.models.PagedGithubIssueResponse
import com.maximeroussy.fizzhub.api.models.PagedGithubRepositorySearchResponse
import io.reactivex.Single

class GithubApi(private val githubEndpoint: GithubEndpoint, private val githubHeaderParser: GithubHeaderParser) {
  fun searchRepositories(query: String, page: Int): Single<PagedGithubRepositorySearchResponse> {
    return githubEndpoint.searchRepositories(query, page)
        .flatMap { response ->
          if (response.isSuccessful) {
            val nextPage = githubHeaderParser.getNextPage(response.headers())
            Single.just(PagedGithubRepositorySearchResponse(nextPage, response.body()!!.items))
          } else {
            Single.error(Throwable(response.errorBody()?.string()))
          }
        }
  }

  fun getIssues(owner: String, repo: String, page: Int): Single<PagedGithubIssueResponse> {
    return githubEndpoint.getIssues(owner, repo, page)
        .flatMap { response ->
          if (response.isSuccessful) {
            val nextPage = githubHeaderParser.getNextPage(response.headers())
            Single.just(PagedGithubIssueResponse(nextPage, response.body()!!))
          } else {
            Single.error(Throwable(response.errorBody()?.string()))
          }
        }
  }

  fun searchIssues(query: String, repoFullName: String, page: Int): Single<PagedGithubIssueResponse> {
    return githubEndpoint.searchIssues("$query+$repoFullName", page)
        .flatMap { response ->
          if (response.isSuccessful) {
            val nextPage = githubHeaderParser.getNextPage(response.headers())
            Single.just(PagedGithubIssueResponse(nextPage, response.body()!!.items))
          } else {
            Single.error(Throwable(response.errorBody()?.string()))
          }
        }
  }
}
