package com.maximeroussy.fizzhub.api

import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BODY
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class ApiModule {
  @Singleton
  @Provides
  fun githubEndpoint(fileManager: FileManager): GithubEndpoint {
    val cacheSize = 10 * 1024 * 1024 // 10 MB
    val cache = Cache(fileManager.getCacheDirectory(), cacheSize.toLong())
    val baseUrl = "https://api.github.com/"
    val httpClient = OkHttpClient.Builder()
        .cache(cache)
        .addInterceptor(HttpLoggingInterceptor().setLevel(BODY))
        .build()
    return Retrofit.Builder()
        .baseUrl(HttpUrl.parse(baseUrl)!!)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(httpClient)
        .build()
        .create(GithubEndpoint::class.java)
  }

  @Provides
  fun githubApi(githubEndpoint: GithubEndpoint, githubHeaderParser: GithubHeaderParser): GithubApi {
    return GithubApi(githubEndpoint, githubHeaderParser)
  }
}
