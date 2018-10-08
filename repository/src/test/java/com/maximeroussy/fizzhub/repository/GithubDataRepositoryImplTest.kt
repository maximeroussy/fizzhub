package com.maximeroussy.fizzhub.repository

import com.maximeroussy.fizzhub.api.GithubApi
import com.maximeroussy.fizzhub.api.models.GithubRepositoryResponse
import com.maximeroussy.fizzhub.api.models.PagedGithubRepositorySearchResponse
import com.maximeroussy.fizzhub.database.GithubRepositoryDao
import com.maximeroussy.fizzhub.database.GithubRepositoryDataModel
import com.maximeroussy.fizzhub.domain.models.GithubRepository
import com.maximeroussy.fizzhub.repository.mappers.GithubIssueMapper
import com.maximeroussy.fizzhub.repository.mappers.GithubRepositoryDataModelMapper
import com.maximeroussy.fizzhub.repository.mappers.GithubRepositoryMapper
import io.mockk.MockKAnnotations
import io.mockk.called
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Single
import org.junit.Before
import org.junit.Test

class GithubDataRepositoryImplTest {
  @MockK private lateinit var githubApi: GithubApi
  @MockK private lateinit var githubRepositoryDao: GithubRepositoryDao
  @MockK private lateinit var githubRepositoryMapper: GithubRepositoryMapper
  @MockK private lateinit var githubRepositoryDataModelMapper: GithubRepositoryDataModelMapper
  @MockK private lateinit var githubIssueMapper: GithubIssueMapper

  private lateinit var sut: GithubDataRepositoryImpl

  @Before
  fun setUp() {
    MockKAnnotations.init(this)
    sut = GithubDataRepositoryImpl(githubApi, githubRepositoryDao, githubRepositoryMapper,
        githubRepositoryDataModelMapper, githubIssueMapper)
  }

  @Test
  fun searchRepositories_callsGithubApi() {
    val searchQuery = "query"
    val page = 0
    val response = PagedGithubRepositorySearchResponse(page, ArrayList())
    every { githubApi.searchRepositories(any(), any()) } returns Single.just(response)

    sut.searchRepositories(searchQuery).test()

    verify { githubApi.searchRepositories(eq(searchQuery), eq(page)) }
  }

  @Test
  fun searchRepositories_mapsSearchResponse() {
    val searchQuery = "query"
    val githubRepositoryResponseList = ArrayList<GithubRepositoryResponse>()
    val pagedGithubRepositorySearchResponse = PagedGithubRepositorySearchResponse(0, githubRepositoryResponseList)
    val localRepositoryData = ArrayList<GithubRepositoryDataModel>()
    val localRepositoryList = ArrayList<GithubRepository>()
    every { githubApi.searchRepositories(any(), any()) } returns Single.just(pagedGithubRepositorySearchResponse)
    every { githubRepositoryDao.getAll() } returns Single.just(localRepositoryData)
    every { githubRepositoryMapper.map(localRepositoryData) } returns localRepositoryList
    every { githubRepositoryMapper.map(any(), any()) } returns ArrayList()

    sut.searchRepositories(searchQuery).test()

    verify { githubRepositoryMapper.map(eq(githubRepositoryResponseList), eq(localRepositoryList)) }
  }

  @Test
  fun loadMoreSearchRepositories_callsGithubApiWithNextPage() {
    val searchQuery = "query"
    val page = 1
    val response = PagedGithubRepositorySearchResponse(page, ArrayList())
    every { githubApi.searchRepositories(any(), any()) } returns Single.just(response)
    every { githubRepositoryDao.getAll() } returns Single.just(ArrayList())
    every { githubRepositoryMapper.map(any()) } returns ArrayList()
    every { githubRepositoryMapper.map(any(), any()) } returns ArrayList()

    sut.searchRepositories(searchQuery).flatMap { sut.loadMoreSearchRepositories(searchQuery) }.test()

    verify { githubApi.searchRepositories(eq(searchQuery), eq(page)) }
  }

  @Test
  fun loadMoreSearchRepositories_doesNotCallGithubApiIfNextPageWasEnd() {
    val searchQuery = "query"
    val page = -1 //signals no more pages
    val response = PagedGithubRepositorySearchResponse(page, ArrayList())
    every { githubApi.searchRepositories(any(), any()) } returns Single.just(response)
    every { githubRepositoryDao.getAll() } returns Single.just(ArrayList())
    every { githubRepositoryMapper.map(any()) } returns ArrayList()
    every { githubRepositoryMapper.map(any(), any()) } returns ArrayList()

    sut.searchRepositories(searchQuery).flatMap { sut.loadMoreSearchRepositories(searchQuery) }.test()

    verify { githubApi.searchRepositories(eq(searchQuery), eq(page)) wasNot called }
  }

  @Test
  fun getAllSavedRepositories_callsGithubRepositoryDao() {
    every { githubRepositoryDao.getAll() } returns Single.just(ArrayList())

    sut.getAllSavedRepositories().test()

    verify { githubRepositoryDao.getAll() }
  }

  @Test
  fun getAllSavedRepositories_mapsDatabaseRecords() {
    val githubRepositoryDataModelList = ArrayList<GithubRepositoryDataModel>()
    every { githubRepositoryDao.getAll() } returns Single.just(githubRepositoryDataModelList)

    sut.getAllSavedRepositories().test()

    verify { githubRepositoryMapper.map(eq(githubRepositoryDataModelList)) }
  }

  @Test
  fun saveRepository_callsGithubRepositoryDao() {
    val githubRepository: GithubRepository = mockk()
    val githubRepositoryDataModel: GithubRepositoryDataModel = mockk()
    every { githubRepositoryDao.insert(any()) } returns Unit
    every { githubRepositoryDataModelMapper.map(any()) } returns githubRepositoryDataModel

    sut.saveRepository(githubRepository).test()

    verify { githubRepositoryDao.insert(eq(githubRepositoryDataModel)) }
  }

  @Test
  fun saveRepository_mapsDomainModelToDatabaseRecord() {
    val githubRepository: GithubRepository = mockk()
    every { githubRepositoryDao.insert(any()) } returns Unit

    sut.saveRepository(githubRepository).test()

    verify { githubRepositoryDataModelMapper.map(eq(githubRepository)) }
  }

  @Test
  fun deleteRepository_callsGithubRepositoryDao() {
    val githubRepository: GithubRepository = mockk()
    val githubRepositoryDataModel: GithubRepositoryDataModel = mockk()
    every { githubRepositoryDao.delete(any()) } returns Unit
    every { githubRepositoryDataModelMapper.map(any()) } returns githubRepositoryDataModel

    sut.deleteRepository(githubRepository).test()

    verify { githubRepositoryDao.delete(eq(githubRepositoryDataModel)) }
  }

  @Test
  fun deleteRepository_mapsDomainModelToDatabaseRecord() {
    val githubRepository: GithubRepository = mockk()
    every { githubRepositoryDao.delete(any()) } returns Unit

    sut.deleteRepository(githubRepository).test()

    verify { githubRepositoryDataModelMapper.map(eq(githubRepository)) }
  }
}
