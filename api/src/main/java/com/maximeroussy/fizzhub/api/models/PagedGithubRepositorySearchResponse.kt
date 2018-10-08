package com.maximeroussy.fizzhub.api.models

data class PagedGithubRepositorySearchResponse(
    val nextPage: Int,
    val items: List<GithubRepositoryResponse>
)
