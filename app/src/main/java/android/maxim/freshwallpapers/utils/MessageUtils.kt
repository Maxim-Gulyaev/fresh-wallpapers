package android.maxim.freshwallpapers.utils

import android.content.Context
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

class MessageUtils @Inject constructor() {
    fun showSnackbar(view: View, message: String, actionTitle: String) {
        val snackbar = Snackbar.make(
            view,
            message,
            Snackbar.LENGTH_INDEFINITE)
        snackbar.setAction(actionTitle) {
            snackbar.dismiss()
        }.show()
    }

    fun showToast(context: Context, message: String) {
        Toast.makeText(
            context,
            message,
            Toast.LENGTH_LONG
        ).show()
    }
}