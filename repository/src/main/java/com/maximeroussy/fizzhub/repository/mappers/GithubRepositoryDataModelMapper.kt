package com.maximeroussy.fizzhub.repository.mappers

import com.maximeroussy.fizzhub.database.GithubRepositoryDataModel
import com.maximeroussy.fizzhub.domain.models.GithubRepository
import javax.inject.Inject

class GithubRepositoryDataModelMapper @Inject constructor() {
  fun map(githubRepository: GithubRepository): GithubRepositoryDataModel {
    return GithubRepositoryDataModel(
        id = githubRepository.id,
        name = githubRepository.name,
        fullName = githubRepository.fullName,
        description = githubRepository.description,
        language = githubRepository.language,
        ownerUsername = githubRepository.ownerUsername,
        repoUrl = githubRepository.repoUrl,
        avatarUrl = githubRepository.avatarUrl,
        stars = githubRepository.stars,
        watchers = githubRepository.watchers
    )
  }
}
