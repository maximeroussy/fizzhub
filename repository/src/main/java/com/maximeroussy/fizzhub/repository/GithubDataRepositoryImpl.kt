package com.maximeroussy.fizzhub.repository

import com.maximeroussy.fizzhub.api.GithubApi
import com.maximeroussy.fizzhub.database.GithubRepositoryDao
import com.maximeroussy.fizzhub.domain.GithubDataRepository
import com.maximeroussy.fizzhub.domain.models.GithubIssue
import com.maximeroussy.fizzhub.domain.models.GithubRepository
import com.maximeroussy.fizzhub.repository.mappers.GithubIssueMapper
import com.maximeroussy.fizzhub.repository.mappers.GithubRepositoryDataModelMapper
import com.maximeroussy.fizzhub.repository.mappers.GithubRepositoryMapper
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GithubDataRepositoryImpl @Inject constructor(
    private val githubApi: GithubApi,
    private val githubRepositoryDao: GithubRepositoryDao,
    private val githubRepositoryMapper: GithubRepositoryMapper,
    private val githubRepositoryDataModelMapper: GithubRepositoryDataModelMapper,
    private val githubIssueMapper: GithubIssueMapper
) : GithubDataRepository {
  private var nextPageIndex = 0
  private val repositoryIssuesNextPageMap = HashMap<String, Int>()

  override fun searchRepositories(query: String): Single<List<GithubRepository>> {
    nextPageIndex = 0
    return loadMoreSearchRepositories(query)
  }

  override fun loadMoreSearchRepositories(query: String): Single<List<GithubRepository>> {
    return if (nextPageIndex == -1) {
      Single.just(ArrayList())
    } else {
      githubApi.searchRepositories(query, nextPageIndex)
          .flatMap { searchResponse ->
            getAllSavedRepositories().map {
              nextPageIndex = searchResponse.nextPage
              githubRepositoryMapper.map(searchResponse.items, it)
            }
          }
    }
  }

  override fun getAllSavedRepositories(): Single<List<GithubRepository>> {
    return githubRepositoryDao.getAll().map { githubRepositoryMapper.map(it) }
  }

  override fun saveRepository(githubRepository: GithubRepository): Completable {
    return Completable
        .fromCallable { githubRepositoryDao.insert(githubRepositoryDataModelMapper.map(githubRepository)) }
  }

  override fun deleteRepository(githubRepository: GithubRepository): Completable {
    return Completable
        .fromCallable { githubRepositoryDao.delete(githubRepositoryDataModelMapper.map(githubRepository)) }
  }

  override fun getIssues(githubRepository: GithubRepository): Single<List<GithubIssue>> {
    repositoryIssuesNextPageMap.clear()
    return getMoreIssues(githubRepository)
  }

  override fun getMoreIssues(githubRepository: GithubRepository): Single<List<GithubIssue>> {
    val repoIssuesNextPage = repositoryIssuesNextPageMap[githubRepository.fullName]
    return if (repoIssuesNextPage == -1) {
      Single.just(ArrayList())
    } else {
      var page = 0
      if (repoIssuesNextPage != null) {
        page = repoIssuesNextPage
      }
      githubApi.getIssues(githubRepository.ownerUsername, githubRepository.name, page)
          .map {
            repositoryIssuesNextPageMap[githubRepository.fullName] = it.nextPage
            githubIssueMapper.map(it.items, githubRepository)
          }
    }
  }

  override fun searchIssues(query: String, githubRepository: GithubRepository): Single<List<GithubIssue>> {
    repositoryIssuesNextPageMap.clear()
    return loadMoreSearchIssues(query, githubRepository)
  }

  override fun loadMoreSearchIssues(query: String, githubRepository: GithubRepository): Single<List<GithubIssue>> {
    val repoIssuesNextPage = repositoryIssuesNextPageMap[githubRepository.fullName]
    return if (repoIssuesNextPage == -1) {
      Single.just(ArrayList())
    } else {
      var page = 0
      if (repoIssuesNextPage != null) {
        page = repoIssuesNextPage
      }
      githubApi.searchIssues(query, githubRepository.fullName, page)
          .map {
            repositoryIssuesNextPageMap[githubRepository.fullName] = it.nextPage
            githubIssueMapper.map(it.items, githubRepository)
          }
    }
  }
}
