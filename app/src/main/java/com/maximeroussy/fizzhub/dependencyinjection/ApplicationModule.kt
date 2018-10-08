package com.maximeroussy.fizzhub.dependencyinjection

import android.content.Context
import com.maximeroussy.fizzhub.api.FileManager
import com.maximeroussy.fizzhub.util.FileManagerImpl
import dagger.Module
import dagger.Provides

@Module
class ApplicationModule(private val context: Context) {
  @Provides
  fun context(): Context = context

  @Provides
  fun fileManager(fileManagerImpl: FileManagerImpl): FileManager = fileManagerImpl
}
