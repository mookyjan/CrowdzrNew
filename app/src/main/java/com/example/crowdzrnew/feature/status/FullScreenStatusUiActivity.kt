package com.example.crowdzrnew.feature.status

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.crowdzrnew.AppPreference
import com.example.crowdzrnew.R
import com.example.crowdzrnew.core.BaseActivity
import com.example.crowdzrnew.core.Router
import com.example.crowdzrnew.core.event.FinishActivityEvent
import com.example.crowdzrnew.core.event.FinishActivityEventModel
import com.example.crowdzrnew.core.event.StartActivityEvent
import com.example.crowdzrnew.core.event.StartActivityModel
import com.example.crowdzrnew.core.ui.FullScreenType
import com.example.crowdzrnew.core.ui.FullScreenUi
import com.example.crowdzrnew.databinding.ActivityFullScreenStatusUiBinding

import com.example.crowdzrnew.feature.MainActivity
import com.example.crowdzrnew.utilities.observe
import dagger.android.AndroidInjection
import javax.inject.Inject

class FullScreenStatusUiActivity : BaseActivity() {

    @Inject
    lateinit var viewModel: FullScreenStatusViewModel

    @Inject
    lateinit var context: Context

    @Inject
    lateinit var router: Router

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
        val binding: ActivityFullScreenStatusUiBinding = DataBindingUtil.setContentView(this, R.layout.activity_full_screen_status_ui)
        val fullScreenUI = intent.getParcelableExtra<FullScreenUi>(Router.Parameter.FULL_SCREEN_UI.name)
        viewModel.fullScreenUI = fullScreenUI
        binding.viewModel = viewModel
//        binding.toolbarModel = ToolbarWithBackModel("", hasBack = false)
        binding.fullScreenUi = fullScreenUI
        viewModel.checkLogin(AppPreference(this).isLoggedIn())
        setupEvents()
    }


    private fun setupEvents() {
        viewModel.backPressEvent.observe(this) {
            if (it!!) onBackPressed()
        }

        viewModel.loginSuccessEvent.observe(this) {
            startActivity(this@FullScreenStatusUiActivity, MainActivity::class.java, clearHistory = true)
            finish()
        }

        viewModel.finishActivityEvent.observe(this@FullScreenStatusUiActivity, object : FinishActivityEvent.FinishActivityObserver {
            override fun onFinishActivity(data: FinishActivityEventModel) {


            }

            override fun onFinishActivityForResult(data: FinishActivityEventModel) {

            }
        })

        viewModel.startActivityEvent.observe(this, object : StartActivityEvent.StartActivityObserver {
            override fun onStartActivity(data: StartActivityModel) {
                startActivity(this@FullScreenStatusUiActivity, Router.getClass(data.to), data.parameters)
                finish()
            }

            override fun onStartActivityForResult(data: StartActivityModel) {
                finish()
            }
        })
    }

    override fun onBackPressed() {
        when (viewModel.fullScreenUI.screenType) {
            FullScreenType.NOCONNECTION -> super.onBackPressed()
            FullScreenType.SERVERERROR -> super.onBackPressed()
            else -> viewModel.onButtonClick()
        }
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        if (isConnected && viewModel.fullScreenUI.screenType == FullScreenType.NOCONNECTION) {
            finish()
        }
    }
}
