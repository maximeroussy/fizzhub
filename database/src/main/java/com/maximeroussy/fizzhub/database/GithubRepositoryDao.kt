package com.maximeroussy.fizzhub.database

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface GithubRepositoryDao {
  @Query("SELECT * from githubRepositoryDataModel")
  fun getAll(): Single<List<GithubRepositoryDataModel>>

  @Insert(onConflict = REPLACE)
  fun insert(githubRepository: GithubRepositoryDataModel)

  @Delete
  fun delete(githubRepository: GithubRepositoryDataModel)
}
