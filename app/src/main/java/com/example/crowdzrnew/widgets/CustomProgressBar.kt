package com.example.crowdzrnew.widgets

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.crowdzrnew.R



/**
 * Created by Firdaus on 14/9/2018.
 */

class CustomProgressBar : Dialog {
    constructor(context: Context) : super(context)

    constructor(context: Context, theme: Int) : super(context, theme)


    override fun onWindowFocusChanged(hasFocus: Boolean) {
        val imageView = findViewById<ImageView>(R.id.spinnerImageView)
        val spinner = imageView.background as AnimationDrawable
        val wrappedSpinner = DrawableCompat.wrap(spinner)
        DrawableCompat.setTint(wrappedSpinner, ContextCompat.getColor(context, R.color.colorPrimary))
        imageView.background = wrappedSpinner
        spinner.start()
    }

    fun setMessage(message: CharSequence?) {
        if (message != null && message.isNotEmpty()) {
            findViewById<TextView>(R.id.message).visibility = View.VISIBLE
            val txt = findViewById<TextView>(R.id.message)
            txt.text = message
            txt.invalidate()
        }
    }

    companion object {

        fun show(context: Context, message: CharSequence?, cancelable: Boolean): CustomProgressBar {
            val dialog = CustomProgressBar(context, R.style.ProgressD)
            dialog.setTitle("")
            dialog.setContentView(R.layout.progress_hud)
            if (message == null || message.isEmpty()) {
                dialog.findViewById<TextView>(R.id.message).visibility = View.VISIBLE
            } else {
                val txt = dialog.findViewById(R.id.message) as TextView
                txt.text = message
            }
            dialog.setCancelable(cancelable)
            dialog.window?.attributes?.gravity = Gravity.CENTER
            val lp = dialog.window?.attributes
            lp?.dimAmount = 0.5f
            dialog.window?.attributes = lp
            dialog.show()
            return dialog
        }
    }
}