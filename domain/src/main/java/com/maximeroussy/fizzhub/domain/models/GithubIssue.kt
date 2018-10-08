package com.maximeroussy.fizzhub.domain.models

import java.io.Serializable

data class GithubIssue(
    val number: Int,
    val title: String,
    val repoName: String,
    val creator: String,
    val body: String,
    val state: String,
    val lastUpdated: String,
    val createdDate: String,
    val issueUrl: String
) : Serializable
