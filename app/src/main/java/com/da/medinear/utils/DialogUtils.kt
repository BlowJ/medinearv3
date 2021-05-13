package com.da.medinear.utils

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context

object DialogUtils {
    private var dialog: Dialog? = null

    fun showProgressDialog(context: Context) {
        dismissProgressDialog()
        dialog = ProgressDialog(context)
            .apply {
                setMessage("Loading...")
                show()
            }
    }

    fun dismissProgressDialog() {
        dialog?.dismiss()
    }
}