package com.example.crowdzrnew.feature.login

import addam.com.my.skeletonApp.core.event.StartActivityEvent
import addam.com.my.skeletonApp.core.event.StartActivityModel
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.crowdzrnew.AppPreference
import com.example.crowdzrnew.R
import com.example.crowdzrnew.core.BaseActivity
import com.example.crowdzrnew.core.Router
import com.example.crowdzrnew.databinding.ActivityLoginBinding
import dagger.android.AndroidInjection
import javax.inject.Inject

class LoginActivity : BaseActivity() {

    @Inject
    lateinit var viewModel: LoginViewModel
    @Inject
    lateinit var preference: AppPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
        val binding : ActivityLoginBinding=DataBindingUtil.setContentView(this,R.layout.activity_login)
        binding.viewmodel=viewModel
        binding.setLifecycleOwner(this)
        setupEvents()
    }

    private fun setupEvents(){
        viewModel.startActivityEvent.observe(this,object :StartActivityEvent.StartActivityObserver{
            override fun onStartActivityForResult(data: StartActivityModel) {
            startActivity(this@LoginActivity,Router.getClass(data.to),data.parameters,data.hasResults)
            }

            override fun onStartActivity(data: StartActivityModel) {
                startActivity(this@LoginActivity,Router.getClass(data.to),data.parameters,data.hasResults)
            }

        })
    }
}
