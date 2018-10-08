package com.maximeroussy.fizzhub.domain

import com.maximeroussy.fizzhub.domain.models.GithubIssue
import com.maximeroussy.fizzhub.domain.models.GithubRepository
import io.reactivex.Completable
import io.reactivex.Single

interface GithubDataRepository {
  fun searchRepositories(query: String): Single<List<GithubRepository>>

  fun loadMoreSearchRepositories(query: String): Single<List<GithubRepository>>

  fun getAllSavedRepositories(): Single<List<GithubRepository>>

  fun saveRepository(githubRepository: GithubRepository): Completable

  fun deleteRepository(githubRepository: GithubRepository): Completable

  fun getIssues(githubRepository: GithubRepository): Single<List<GithubIssue>>

  fun getMoreIssues(githubRepository: GithubRepository): Single<List<GithubIssue>>

  fun searchIssues(query: String, githubRepository: GithubRepository): Single<List<GithubIssue>>

  fun loadMoreSearchIssues(query: String, githubRepository: GithubRepository): Single<List<GithubIssue>>
}
