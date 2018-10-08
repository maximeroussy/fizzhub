package com.maximeroussy.fizzhub.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

@Database(entities = [(GithubRepositoryDataModel::class)], version = 1)
abstract class GithubRepositoryDatabase : RoomDatabase() {
  abstract fun githubRepositoryDao(): GithubRepositoryDao
}
