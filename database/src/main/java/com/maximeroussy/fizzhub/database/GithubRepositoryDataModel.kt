package com.maximeroussy.fizzhub.database

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class GithubRepositoryDataModel(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "full_name") val fullName: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "language") val language: String,
    @ColumnInfo(name = "owner_username") val ownerUsername: String,
    @ColumnInfo(name = "repo_url") val repoUrl: String,
    @ColumnInfo(name = "avatar_url") val avatarUrl: String,
    @ColumnInfo(name = "stars") val stars: Int,
    @ColumnInfo(name = "watchers") val watchers: Int
)
