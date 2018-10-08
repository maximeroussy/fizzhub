package com.maximeroussy.fizzhub.repository.mappers

import com.maximeroussy.fizzhub.domain.models.GithubRepository
import org.junit.Before

import org.junit.Assert.*
import org.junit.Test

class GithubRepositoryDataModelMapperTest {
  private lateinit var sut: GithubRepositoryDataModelMapper
  @Before
  fun setUp() {
    sut = GithubRepositoryDataModelMapper()
  }

  @Test
  fun map_returnsProperGithubRepositoryDataModel() {
    val id = 5L
    val name = "name"
    val fullName = "full_name"
    val description = "description"
    val language = "language"
    val ownerUsername = "owner_username"
    val repoUrl = "repo_url"
    val avatarUrl = "avatar_url"
    val stars = 5
    val watchers = 5
    val githubRepository = GithubRepository(id, name, fullName, description, language, ownerUsername, repoUrl,
        avatarUrl, stars, watchers, false)

    val result = sut.map(githubRepository)

    assertEquals(result.name, name)
    assertEquals(result.fullName, fullName)
    assertEquals(result.description, description)
    assertEquals(result.language, language)
    assertEquals(result.ownerUsername, ownerUsername)
    assertEquals(result.repoUrl, repoUrl)
    assertEquals(result.avatarUrl, avatarUrl)
    assertEquals(result.stars, stars)
    assertEquals(result.watchers, watchers)
  }
}
