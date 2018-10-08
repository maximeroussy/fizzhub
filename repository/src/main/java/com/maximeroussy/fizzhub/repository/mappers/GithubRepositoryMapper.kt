package com.maximeroussy.fizzhub.repository.mappers

import com.maximeroussy.fizzhub.api.models.GithubRepositoryResponse
import com.maximeroussy.fizzhub.database.GithubRepositoryDataModel
import com.maximeroussy.fizzhub.domain.models.GithubRepository
import javax.inject.Inject

class GithubRepositoryMapper @Inject constructor() {
  fun map(githubRepositoryResponseList: List<GithubRepositoryResponse>,
      localRepositoryList: List<GithubRepository>): List<GithubRepository> {
    return githubRepositoryResponseList.map {
      val matches = getMatchingLocalRepository(it, localRepositoryList)
      if (matches.isNotEmpty()) {
        map(it, true, matches.first().id)
      } else {
        map(it, false, 0)
      }
    }
  }

  private fun getMatchingLocalRepository(githubRepositoryResponse: GithubRepositoryResponse,
      localRepositoryList: List<GithubRepository>): List<GithubRepository> {
    return localRepositoryList.filter { it.fullName == githubRepositoryResponse.fullName }
  }

  private fun map(githubRepositoryResponse: GithubRepositoryResponse, tracked: Boolean, id: Long): GithubRepository {
    return GithubRepository(
        id = id,
        name = githubRepositoryResponse.name,
        fullName = githubRepositoryResponse.fullName,
        description = githubRepositoryResponse.description ?: "",
        language = githubRepositoryResponse.language ?: "",
        ownerUsername = githubRepositoryResponse.owner.login,
        repoUrl = githubRepositoryResponse.htmlUrl,
        avatarUrl = githubRepositoryResponse.owner.avatarUrl,
        stars = githubRepositoryResponse.stargazersCount,
        watchers = githubRepositoryResponse.watchersCount,
        tracked = tracked
    )
  }

  fun map(githubRepositoryDataModelList: List<GithubRepositoryDataModel>): List<GithubRepository> {
    return githubRepositoryDataModelList.map { map(it) }
  }

  private fun map(githubRepositoryDataModel: GithubRepositoryDataModel): GithubRepository {
    return GithubRepository(
        githubRepositoryDataModel.id,
        githubRepositoryDataModel.name,
        githubRepositoryDataModel.fullName,
        githubRepositoryDataModel.description,
        githubRepositoryDataModel.language,
        githubRepositoryDataModel.ownerUsername,
        githubRepositoryDataModel.repoUrl,
        githubRepositoryDataModel.avatarUrl,
        githubRepositoryDataModel.stars,
        githubRepositoryDataModel.watchers,
        false
    )
  }
}
