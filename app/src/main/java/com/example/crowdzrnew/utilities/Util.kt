package com.example.crowdzrnew.utilities

import android.graphics.Color
import android.os.Build
import android.support.design.widget.Snackbar
import android.view.View
import android.view.ViewTreeObserver
import android.widget.TextView

fun unsignedLong(mostSignificantBits: Long, leastSignificantBits: Long) =
        (mostSignificantBits shl 32) or leastSignificantBits

fun showSnackbar(view: View, text: String?) {
    if (text != null && text.isNotBlank()) {
        val snackbar = Snackbar.make(view, text, Snackbar.LENGTH_LONG)
        val snackbarView = snackbar.view
        val textView = snackbarView.findViewById(android.support.design.R.id.snackbar_text) as TextView?
        if (textView != null) {
            textView.setTextColor(Color.WHITE)
            textView.maxLines = 3
        }
        snackbar.show()
    }
}

inline fun View.waitForLayout(crossinline f: () -> Unit) = with(viewTreeObserver) {
    addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            viewTreeObserver.removeOnGlobalLayoutListener(this)
            f()
        }
    })
}

fun isEmulator(): Boolean {
    return (Build.FINGERPRINT.startsWith("generic")
            || Build.FINGERPRINT.startsWith("unknown")
            || Build.MODEL.contains("google_sdk")
            || Build.MODEL.contains("Emulator")
            || Build.MODEL.contains("Android SDK built for x86")
            || Build.MANUFACTURER.contains("Genymotion")
            || Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic")
            || "google_sdk" == Build.PRODUCT)
}
