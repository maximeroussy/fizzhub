package com.maximeroussy.fizzhub.domain.usecases

import com.maximeroussy.fizzhub.domain.GithubDataRepository
import com.maximeroussy.fizzhub.domain.models.GithubIssue
import io.reactivex.Single
import javax.inject.Inject

class GetAllIssues @Inject constructor(private val githubDataRepository: GithubDataRepository) {
  fun first(): Single<List<GithubIssue>> {
    return githubDataRepository.getAllSavedRepositories()
        .flattenAsObservable { it }
        .flatMap { githubDataRepository.getIssues(it).toObservable() }
        .toList()
        .flatMap { list ->
          Single.just(list.flatten().sortedBy { it.lastUpdated }.reversed())
        }
  }

  fun next(): Single<List<GithubIssue>> {
    return githubDataRepository.getAllSavedRepositories()
        .flattenAsObservable { it }
        .flatMap { githubDataRepository.getMoreIssues(it).toObservable() }
        .toList()
        .flatMap { list ->
          Single.just(list.flatten().sortedBy { it.lastUpdated }.reversed())
        }
  }
}
