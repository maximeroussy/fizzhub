package com.maximeroussy.fizzhub.repository

import com.maximeroussy.fizzhub.domain.GithubDataRepository
import dagger.Module
import dagger.Provides

@Module
class RepositoryModule {
  @Provides
  fun githubDataRepository(githubDataRepositoryImpl: GithubDataRepositoryImpl): GithubDataRepository {
    return githubDataRepositoryImpl
  }
}
