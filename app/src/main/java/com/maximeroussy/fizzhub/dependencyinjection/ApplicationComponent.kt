package com.maximeroussy.fizzhub.dependencyinjection

import com.maximeroussy.fizzhub.api.ApiModule
import com.maximeroussy.fizzhub.database.DatabaseModule
import com.maximeroussy.fizzhub.presentation.issuelist.IssueListFragment
import com.maximeroussy.fizzhub.presentation.repositorydetail.RepositoryDetailActivity
import com.maximeroussy.fizzhub.presentation.repositorylist.RepositoryListFragment
import com.maximeroussy.fizzhub.presentation.repositorysearch.RepositorySearchActivity
import com.maximeroussy.fizzhub.repository.RepositoryModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class, RepositoryModule::class, ApiModule::class, DatabaseModule::class])
interface ApplicationComponent {
  fun inject(repositoryListFragment: RepositoryListFragment)
  fun inject(issueListFragment: IssueListFragment)
  fun inject(repositorySearchActivity: RepositorySearchActivity)
  fun inject(repositoryDetailActivity: RepositoryDetailActivity)
}
