package com.maximeroussy.fizzhub.api

import java.io.File

interface FileManager {
  fun getCacheDirectory(): File
}
