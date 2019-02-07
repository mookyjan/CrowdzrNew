package com.example.crowdzrnew.core.event


import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import com.example.crowdzrnew.core.Router

/**
 * Created by Addam on 7/1/2019.
 */
open class FinishActivityEvent : SingleLiveEvent<FinishActivityEventModel>() {

    fun observe(owner: LifecycleOwner, observer: FinishActivityObserver){
        super.observe(owner, Observer { t ->
            if (t == null) {
                return@Observer
            }
            if(t.hasResults) {
                observer.onFinishActivityForResult(t)
            } else {
                observer.onFinishActivity(t)
            }
        })
    }

    interface FinishActivityObserver {
        fun onFinishActivity(data: FinishActivityEventModel)
        fun onFinishActivityForResult(data: FinishActivityEventModel)
    }

}


data class FinishActivityEventModel (val parameters: HashMap<Router.Parameter, Any?> = hashMapOf(), val hasResults : Boolean = false)