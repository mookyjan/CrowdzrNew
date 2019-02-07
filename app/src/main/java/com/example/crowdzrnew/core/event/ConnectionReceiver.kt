package com.example.crowdzrnew.core.event

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager

/**
 * Created by Owner on 2/1/2018.
 */

class ConnectionReceiver : BroadcastReceiver() {

    private lateinit var context: Context
    private var listener: ConnectionReceiverListener? = null

    override fun onReceive(context: Context, p1: Intent?) {
        this.context = context
        listener?.onNetworkConnectionChanged(checkConnection(context))
    }

    fun setListener(listener: ConnectionReceiverListener) {
        this.listener = listener
    }

    fun isConnected(): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo

        return (activeNetwork != null && activeNetwork.isConnectedOrConnecting)
    }

    interface ConnectionReceiverListener {
        fun onNetworkConnectionChanged(isConnected: Boolean)
    }

    private fun checkConnection(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo

        return (activeNetwork != null && activeNetwork.isConnectedOrConnecting)
    }

}