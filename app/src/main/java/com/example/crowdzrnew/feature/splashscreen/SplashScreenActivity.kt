package com.example.crowdzrnew.feature.splashscreen

import android.app.AlertDialog
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Handler
import com.example.crowdzrnew.AppPreference
import com.example.crowdzrnew.R
import com.example.crowdzrnew.core.BaseActivity
import com.example.crowdzrnew.core.Router
import com.example.crowdzrnew.core.event.StartActivityEvent
import com.example.crowdzrnew.core.event.StartActivityModel
import com.example.crowdzrnew.databinding.ActivitySplashScreenBinding
import dagger.android.AndroidInjection
import javax.inject.Inject

class SplashScreenActivity : BaseActivity(), SplashScreenViewModel.SplashScreenListener {

    @Inject
    lateinit var viewModel: SplashScreenViewModel
    private var mDelayHandler: Handler? = null
    private var SPLASH_DELAY: Long = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
        val binding: ActivitySplashScreenBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash_screen)
        binding.viewmodel = viewModel
        viewModel.splashScreenListener = this
        setupEvents()
        mDelayHandler = Handler()
    }


    private fun setupEvents() {
        viewModel.startActivityEvent.observe(this@SplashScreenActivity,
            object : StartActivityEvent.StartActivityObserver {
                override fun onStartActivity(data: StartActivityModel) {
                    startActivity(this@SplashScreenActivity, Router.getClass(data.to), clearHistory = true)
                }

                override fun onStartActivityForResult(data: StartActivityModel) {
                    startActivityForResult(this@SplashScreenActivity, Router.getClass(data.to), data.parameters, 100)
                }
            })
    }

    private val mRunnable: Runnable = Runnable {

        if (!isFinishing) {
            val pref = AppPreference(this)
            if (pref.isFirstRun()) {
                startActivity(this@SplashScreenActivity, Router.getClass(Router.Destination.LOGIN), clearHistory = true)
            } else {
                viewModel.redirect()
            }
        }
    }

    public override fun onDestroy() {
        if (mDelayHandler != null) {
            mDelayHandler!!.removeCallbacks(mRunnable)
        }

        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == 200) {
            viewModel.startActivityEvent.value = StartActivityModel(Router.Destination.MAIN)
        } else {
            finish()
        }
    }

    override fun showErrorDialog(msgId: Int, updateUrl: String?, message: String?) {
        if (updateUrl != null && message != null) {
            val dialog = AlertDialog.Builder(this)

                .setNegativeButton(android.R.string.cancel) { dialog, which -> finish() }
            dialog.show()
        } else {
            val builder = AlertDialog.Builder(this)
            builder.setMessage(getString(msgId))
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok) { dialog, which ->
                    finish()
                }
                .show()
        }
    }

    override fun proceed() {
        mDelayHandler!!.postDelayed(mRunnable, SPLASH_DELAY)
    }
}
