package com.maximeroussy.fizzhub.repository.mappers

import android.annotation.SuppressLint
import com.maximeroussy.fizzhub.api.models.GithubIssueResponse
import com.maximeroussy.fizzhub.domain.models.GithubIssue
import com.maximeroussy.fizzhub.domain.models.GithubRepository
import java.text.SimpleDateFormat
import javax.inject.Inject

class GithubIssueMapper @Inject constructor() {
  fun map(githubIssueResponseList: List<GithubIssueResponse>, githubRepository: GithubRepository): List<GithubIssue> {
    return githubIssueResponseList.map { map(it, githubRepository.fullName) }
  }

  fun map(githubIssueResponse: GithubIssueResponse, repoName: String): GithubIssue {
    return GithubIssue(
        githubIssueResponse.number,
        githubIssueResponse.title,
        repoName,
        githubIssueResponse.user.login,
        githubIssueResponse.body,
        githubIssueResponse.state,
        mapDateFormats(githubIssueResponse.updatedAt),
        mapDateFormats(githubIssueResponse.createdAt),
        githubIssueResponse.htmlUrl
    )
  }

  @SuppressLint("SimpleDateFormat")
  private fun mapDateFormats(githubDate: String): String {
    val date = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(githubDate)
    return SimpleDateFormat("yyyy/MM/dd").format(date)
  }
}
