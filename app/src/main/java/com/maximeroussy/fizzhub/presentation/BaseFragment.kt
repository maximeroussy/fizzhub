package com.maximeroussy.fizzhub.presentation

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.maximeroussy.fizzhub.R

abstract class BaseFragment<VDB : ViewDataBinding>(@LayoutRes private var layoutResourceId: Int) : Fragment() {

  internal lateinit var binding: VDB

  final override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
    binding = DataBindingUtil.inflate(inflater, layoutResourceId, container, false)
    return binding.root
  }

  protected fun showShortSnackbar(messageResId: Int) {
    activity?.let {
      Snackbar.make(it.findViewById(R.id.container), messageResId, Snackbar.LENGTH_SHORT).show()
    }
  }

  protected fun showErrorDialog(messageResId: Int) {
    activity?.let {
      val builder = AlertDialog.Builder(it)
      builder.setTitle(R.string.error)
      builder.setMessage(messageResId)
      builder.setPositiveButton(R.string.ok) { dialog, _ -> dialog.dismiss() }
      builder.show()
    }
  }
}
