package com.maximeroussy.fizzhub.domain.usecases

import com.maximeroussy.fizzhub.domain.GithubDataRepository
import com.maximeroussy.fizzhub.domain.models.GithubIssue
import io.reactivex.Single
import javax.inject.Inject

class SearchIssues @Inject constructor(private val githubDataRepository: GithubDataRepository) {
  fun first(query: String): Single<List<GithubIssue>> {
    return githubDataRepository.getAllSavedRepositories()
        .flattenAsObservable { it }
        .flatMap { githubDataRepository.searchIssues(query, it).toObservable() }
        .toList()
        .flatMap { list ->
          Single.just(list.flatten().sortedBy { it.lastUpdated }.reversed())
        }
  }

  fun next(query: String): Single<List<GithubIssue>> {
    return githubDataRepository.getAllSavedRepositories()
        .flattenAsObservable { it }
        .flatMap { githubDataRepository.loadMoreSearchIssues(query, it).toObservable() }
        .toList()
        .flatMap { list ->
          Single.just(list.flatten().sortedBy { it.lastUpdated }.reversed())
        }
  }
}
