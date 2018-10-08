package com.maximeroussy.fizzhub.domain.models

import java.io.Serializable

data class GithubRepository(
    val id: Long = 0,
    val name: String,
    val fullName: String,
    val description: String,
    val language: String,
    val ownerUsername: String,
    val repoUrl: String,
    val avatarUrl: String,
    val stars: Int,
    val watchers: Int,
    val tracked: Boolean
) : Serializable
