package com.maximeroussy.fizzhub.api.models

import com.google.gson.annotations.SerializedName

data class GithubIssueSearchResponse(
    @SerializedName("total_count") val totalCount: Int,
    @SerializedName("incomplete_results") val incompleteResults: Boolean,
    @SerializedName("items") val items: List<GithubIssueResponse>
)
