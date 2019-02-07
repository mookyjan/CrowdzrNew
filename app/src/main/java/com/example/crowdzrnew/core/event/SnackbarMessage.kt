package com.example.crowdzrnew.core.event

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.support.annotation.StringRes


/**
 * Created by Arman on 11/15/2017.
 */

open class SnackbarMessage : SingleLiveEvent<Any>() {
    fun observe(owner: LifecycleOwner, observer: SnackbarObserver) {
        super.observe(owner, Observer { t ->
            if (t == null) {
                return@Observer
            }
            if(t is String) {
                observer.onNewMessage(t)
            } else if( t is Int) {
                observer.onNewMessage(t)
            }
        })
    }

    interface SnackbarObserver {
        /**
         * Called when there is a new message to be shown.
         * @param snackbarMessageResourceId The new message, non-null.
         */
        fun onNewMessage(@StringRes snackbarMessageResourceId: Int)
        fun onNewMessage(message: String)
    }
}