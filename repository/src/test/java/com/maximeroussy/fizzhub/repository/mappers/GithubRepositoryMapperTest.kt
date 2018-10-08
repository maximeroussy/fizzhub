package com.maximeroussy.fizzhub.repository.mappers

import com.maximeroussy.fizzhub.api.models.GithubRepositoryResponse
import com.maximeroussy.fizzhub.api.models.GithubRepositoryResponse.Owner
import com.maximeroussy.fizzhub.database.GithubRepositoryDataModel
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GithubRepositoryMapperTest {
  private lateinit var sut: GithubRepositoryMapper

  @Before
  fun setUp() {
    sut = GithubRepositoryMapper()
  }

  @Test
  fun map_returnsSameNumberOfRepositoriesMappedFromGithubRepositorySearchResponse() {
    val name = "name"
    val fullName = "full_name"
    val description = "description"
    val language = "language"
    val ownerUsername = "owner_username"
    val repoUrl = "repo_url"
    val avatarUrl = "avatar_url"
    val stars = 5
    val watchers = 5
    val ownerResponse = Owner(ownerUsername, 0, "", avatarUrl, "","","","")
    val githubRepositoryResponseList = ArrayList<GithubRepositoryResponse>()
    val numberOfRepositories = 5
    for (i in 0..(numberOfRepositories - 1)) {
      val repositoryResponse = GithubRepositoryResponse(0, "", name + i, fullName, ownerResponse, false, repoUrl,
          description,
          false, "", "", "", "", "", 0, stars, watchers, language, 0, 0, "", "", 0.0)
      githubRepositoryResponseList.add(repositoryResponse)
    }

    val result = sut.map(githubRepositoryResponseList, ArrayList())

    assertEquals(result.size, numberOfRepositories)
  }

  @Test
  fun map_returnsProperlyMappedRepositoriesFromGithubRepositorySearchResponse() {
    val name = "name"
    val fullName = "full_name"
    val description = "description"
    val language = "language"
    val ownerUsername = "owner_username"
    val repoUrl = "repo_url"
    val avatarUrl = "avatar_url"
    val stars = 5
    val watchers = 5
    val ownerResponse = Owner(ownerUsername, 0, "", avatarUrl, "","","","")
    val githubRepositoryResponseList = ArrayList<GithubRepositoryResponse>()
    for (i in 0..4) {
      val repositoryResponse = GithubRepositoryResponse(0, "", name + i, fullName, ownerResponse, false, repoUrl,
          description,
          false, "", "", "", "", "", 0, stars, watchers, language, 0, 0, "", "", 0.0)
      githubRepositoryResponseList.add(repositoryResponse)
    }

    val result = sut.map(githubRepositoryResponseList, ArrayList())

    assertEquals(result[2].name, name + 2)
    assertEquals(result[2].fullName, fullName)
    assertEquals(result[2].description, description)
    assertEquals(result[2].language, language)
    assertEquals(result[2].ownerUsername, ownerUsername)
    assertEquals(result[2].repoUrl, repoUrl)
    assertEquals(result[2].avatarUrl, avatarUrl)
    assertEquals(result[2].stars, stars)
    assertEquals(result[2].watchers, watchers)
  }

  @Test
  fun map_returnsSameNumberOfRepositoriesMappedFromGithubRepositoryDataModel() {
    val name = "name"
    val fullName = "full_name"
    val description = "description"
    val language = "language"
    val ownerUsername = "owner_username"
    val repoUrl = "repo_url"
    val avatarUrl = "avatar_url"
    val stars = 5
    val watchers = 5
    val githubRepositoryDataModelList = ArrayList<GithubRepositoryDataModel>()
    val numberOfRepositories = 5
    for (i in 0..(numberOfRepositories - 1)) {
      val githubRepositoryDataModel = GithubRepositoryDataModel(
          name = name + i,
          fullName =  fullName,
          description = description,
          language = language,
          ownerUsername = ownerUsername,
          repoUrl = repoUrl,
          avatarUrl = avatarUrl,
          stars = stars,
          watchers = watchers
      )
      githubRepositoryDataModelList.add(githubRepositoryDataModel)
    }

    val result = sut.map(githubRepositoryDataModelList)

    assertEquals(result.size, numberOfRepositories)
  }

  @Test
  fun map_returnsProperlyMappedRepositoriesFromGithubRepositoryDataModel() {
    val name = "name"
    val fullName = "full_name"
    val description = "description"
    val language = "language"
    val ownerUsername = "owner_username"
    val repoUrl = "repo_url"
    val avatarUrl = "avatar_url"
    val stars = 5
    val watchers = 5
    val githubRepositoryDataModelList = ArrayList<GithubRepositoryDataModel>()
    val numberOfRepositories = 5
    for (i in 0..(numberOfRepositories - 1)) {
      val githubRepositoryDataModel = GithubRepositoryDataModel(
          name = name + i,
          fullName =  fullName,
          description = description,
          language = language,
          ownerUsername = ownerUsername,
          repoUrl = repoUrl,
          avatarUrl = avatarUrl,
          stars = stars,
          watchers = watchers
      )
      githubRepositoryDataModelList.add(githubRepositoryDataModel)
    }

    val result = sut.map(githubRepositoryDataModelList)

    assertEquals(result[2].name, name + 2)
    assertEquals(result[2].fullName, fullName)
    assertEquals(result[2].description, description)
    assertEquals(result[2].language, language)
    assertEquals(result[2].ownerUsername, ownerUsername)
    assertEquals(result[2].repoUrl, repoUrl)
    assertEquals(result[2].avatarUrl, avatarUrl)
    assertEquals(result[2].stars, stars)
    assertEquals(result[2].watchers, watchers)
  }
}
