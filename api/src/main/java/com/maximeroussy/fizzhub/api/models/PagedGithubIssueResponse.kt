package com.maximeroussy.fizzhub.api.models

data class PagedGithubIssueResponse(
    val nextPage: Int,
    val items: List<GithubIssueResponse>
)
