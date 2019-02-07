package com.example.crowdzrnew.core

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager
import com.example.crowdzrnew.R
import com.example.crowdzrnew.core.event.ConnectionReceiver
import com.example.crowdzrnew.core.ui.FullScreenType
import com.example.crowdzrnew.core.ui.FullScreenUi
import com.example.crowdzrnew.feature.status.FullScreenStatusUiActivity
import com.example.crowdzrnew.widgets.CustomProgressBar
import com.github.ajalt.timberkt.Timber

/**
 * Created by Addam on 7/1/2019.
 */
open class BaseActivity: AppCompatActivity() , ConnectionReceiver.ConnectionReceiverListener{
    private var progressBarDialog: CustomProgressBar? = null
    private val connectionReceiver = ConnectionReceiver()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE)

//        if (toolbar == null) {
//            Timber.e { "Toolbar is null" }
//        }
//        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        setupListeners()
    }

    private fun setupListeners() {
        applicationContext.registerReceiver(connectionReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
        connectionReceiver.setListener(this)
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    fun startActivity(from: Context, to: Class<*>, parameters: HashMap<Router.Parameter, Any?> = hashMapOf(), clearHistory: Boolean = false, singleTask: Boolean = false) {
        val intent = Intent(from, to)
        if (singleTask) {
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        }
        if (clearHistory) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
        if (parameters.isNotEmpty()) {
            intent.putExtras(parameters.bundle)
        }
        startActivity(intent)
    }

    fun startActivity(from: Context, to: Class<*>, parameters: HashMap<Router.Parameter, Any?> = hashMapOf(), clearHistory: Boolean = false, singleTask: Boolean = false, transition: Bundle) {
        val intent = Intent(from, to)
        if (singleTask) {
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        }
        if (clearHistory) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
        if (parameters.isNotEmpty()) {
            intent.putExtras(parameters.bundle)
        }
        if (transition != Bundle.EMPTY) {
            startActivity(intent, transition)
        } else {
            startActivity(intent)
        }
    }

    fun startActivityForResult(from: Context, to: Class<*>, parameters: HashMap<Router.Parameter, Any?> = hashMapOf(), requestCode: Int, singleTask: Boolean = false) {
        val intent = Intent(from, to)
        if (singleTask) {
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        }
        if (parameters.isNotEmpty()) {
            intent.putExtras(parameters.bundle)
        }
        startActivityForResult(intent, requestCode)
    }

    fun startActivityForResult(from: Context, to: Class<*>, parameters: HashMap<Router.Parameter, Any?> = hashMapOf(), requestCode: Int, singleTask: Boolean = false, transition: Bundle) {
        val intent = Intent(from, to)
        if (singleTask) {
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        }
        if (parameters.isNotEmpty()) {
            intent.putExtras(parameters.bundle)
        }
        if (transition != Bundle.EMPTY) {
            startActivityForResult(intent, requestCode, transition)
        } else {
            startActivityForResult(intent, requestCode)
        }
    }

    protected fun showLoading(isShown: Boolean?, string: Any = R.string.progress_bar_message) {
        if (!isFinishing) {
            if (isShown!!) {
                if (progressBarDialog == null) {
                    createProgressBar(string)
                }
                progressBarDialog?.show()
            } else {
                if (progressBarDialog == null) {
                    createProgressBar(string)
                }
                progressBarDialog?.dismiss()
            }
        }
    }
    private fun createProgressBar(string: Any) {
        progressBarDialog = CustomProgressBar.show(this, if (string is Int) getString(string) else string as String, false)
    }
    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        if (!isConnected) {
            val fullScreenUI = FullScreenUi(FullScreenType.NOCONNECTION)
            startActivity(this@BaseActivity, FullScreenStatusUiActivity::class.java, hashMapOf(Pair(Router.Parameter.FULL_SCREEN_UI, fullScreenUI)), singleTask = true)
        }
    }
}