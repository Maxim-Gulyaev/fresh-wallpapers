package android.maxim.freshwallpapers.utils

import android.view.View
import com.google.android.material.snackbar.Snackbar

class SnackbarUtils {
    fun showSnackbar(view: View, message: String, actionTitle: String) {
        val snackbar = Snackbar.make(
            view,
            message,
            Snackbar.LENGTH_INDEFINITE)
        snackbar.setAction(actionTitle) {
            snackbar.dismiss()
        }.show()
    }
}