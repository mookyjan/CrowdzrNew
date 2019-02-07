package com.example.crowdzrnew

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import com.example.crowdzrnew.di.DaggerAppComponent
import com.github.ajalt.timberkt.BuildConfig
import com.github.ajalt.timberkt.Timber
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import io.reactivex.annotations.NonNull
import java.lang.ref.WeakReference
import javax.inject.Inject

class CrowdZrApp: Application(),HasActivityInjector,Application.ActivityLifecycleCallbacks {

    @Inject
    lateinit var activityDispatchingAndroidInjector: DispatchingAndroidInjector<Activity>
    private var foregroundActivity: WeakReference<Context>? = null
    override fun onCreate() {
        super.onCreate()
        initLogger()
        DaggerAppComponent.builder()
            .application(this)
            .build()
            .inject(this)
    }

    private fun initLogger() {
        if (BuildConfig.DEBUG) {
            Timber.plant(timber.log.Timber.DebugTree())
        }
    }

    override fun activityInjector(): AndroidInjector<Activity> = activityDispatchingAndroidInjector

    override fun onTerminate() {
        super.onTerminate()
        unregisterActivityLifecycleCallbacks(this)
    }

    override fun onActivityPaused(activity: Activity?) {
    }

    override fun onActivityResumed(activity: Activity?) {
    }

    override fun onActivityStarted(activity: Activity?) {
        foregroundActivity = WeakReference<Context>(activity)
    }

    override fun onActivityDestroyed(activity: Activity?) {
    }

    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
    }

    override fun onActivityStopped(activity: Activity?) {
        val canonicalName = activity?.javaClass?.canonicalName
        if (foregroundActivity != null && foregroundActivity?.get()?.javaClass?.canonicalName.equals(canonicalName)) {
            foregroundActivity = null
        }
    }

    override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
    }
    fun isOnForeground(@NonNull context: Context): Boolean {
        return if (context is CrowdZrApp) {
            isOnForeground(foregroundActivity?.get()?.javaClass?.canonicalName)
        } else {
            isOnForeground(context.javaClass.canonicalName)
        }
    }

    private fun isOnForeground(@NonNull activityCanonicalName: String?): Boolean {
        return if (foregroundActivity?.get() != null) {
            return foregroundActivity?.get()?.javaClass?.canonicalName == activityCanonicalName
        } else false
    }
}