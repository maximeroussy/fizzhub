package com.maximeroussy.fizzhub.util

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import javax.inject.Inject
import dagger.Lazy

class ViewModelFactory<T : ViewModel> @Inject constructor(private val viewModel: Lazy<T>) : ViewModelProvider.Factory {
  @Suppress("UNCHECKED_CAST")
  override fun <T : ViewModel?> create(modelClass: Class<T>): T = viewModel.get() as T
}
