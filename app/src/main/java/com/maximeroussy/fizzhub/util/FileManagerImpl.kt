package com.maximeroussy.fizzhub.util

import android.content.Context
import com.maximeroussy.fizzhub.api.FileManager
import java.io.File
import javax.inject.Inject

class FileManagerImpl @Inject constructor(private val context: Context) : FileManager {
  override fun getCacheDirectory(): File {
    return context.cacheDir
  }
}
