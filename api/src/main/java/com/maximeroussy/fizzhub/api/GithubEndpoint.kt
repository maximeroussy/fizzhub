package com.maximeroussy.fizzhub.api

import com.maximeroussy.fizzhub.api.models.GithubIssueResponse
import com.maximeroussy.fizzhub.api.models.GithubIssueSearchResponse
import com.maximeroussy.fizzhub.api.models.GithubRepositorySearchResponse
import io.reactivex.Single
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.logging.Level
import java.util.logging.Logger

interface GithubEndpoint {
  @GET("/search/repositories?per_page=20")
  fun searchRepositories(@Query("q") query: String, @Query("page") page: Int):
      Single<Response<GithubRepositorySearchResponse>>

  @GET("/repos/{owner}/{repo}/issues?per_page=10")
  fun getIssues(@Path("owner") owner: String, @Path("repo") repo: String, @Query("page") page: Int):
      Single<Response<List<GithubIssueResponse>>>

  @GET("/search/issues?per_page=10&sort=updated")
  fun searchIssues(@Query("q") query: String, @Query("page") page: Int): Single<Response<GithubIssueSearchResponse>>
}
