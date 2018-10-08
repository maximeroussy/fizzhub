package com.maximeroussy.fizzhub.database

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import android.arch.persistence.room.Room

@Module
class DatabaseModule {
  @Singleton
  @Provides
  fun githubRepositoryDatabase(context: Context): GithubRepositoryDatabase {
    return Room.databaseBuilder(context, GithubRepositoryDatabase::class.java, "fizzhub-db").build()
  }

  @Singleton
  @Provides
  fun githubRepositoryDao(githubRepositoryDatabase: GithubRepositoryDatabase): GithubRepositoryDao {
    return githubRepositoryDatabase.githubRepositoryDao()
  }
}
