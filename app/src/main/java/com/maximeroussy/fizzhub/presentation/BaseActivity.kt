package com.maximeroussy.fizzhub.presentation

import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import com.maximeroussy.fizzhub.R

abstract class BaseActivity : AppCompatActivity() {
  fun showShortSnackbar(messageResId: Int) {
    Snackbar.make(findViewById(R.id.container), messageResId, Snackbar.LENGTH_SHORT).show()
  }

  fun showErrorDialog(messageResId: Int) {
    val builder = AlertDialog.Builder(this)
    builder.setTitle(R.string.error)
    builder.setMessage(messageResId)
    builder.setPositiveButton(R.string.ok) { dialog, _ -> dialog.dismiss() }
    builder.show()
  }
}
