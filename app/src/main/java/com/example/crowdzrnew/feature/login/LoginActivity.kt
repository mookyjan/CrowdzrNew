package com.example.crowdzrnew.feature.login


import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.AppCompatEditText
import android.view.View
import android.widget.EditText
import com.example.crowdzrnew.AppPreference
import com.example.crowdzrnew.R
import com.example.crowdzrnew.core.BaseActivity
import com.example.crowdzrnew.core.Router
import com.example.crowdzrnew.core.event.SnackbarMessage
import com.example.crowdzrnew.core.event.StartActivityEvent
import com.example.crowdzrnew.core.event.StartActivityModel
import com.example.crowdzrnew.databinding.ActivityLoginBinding
import com.example.crowdzrnew.utilities.observe
import com.example.crowdzrnew.utilities.showSnackbar
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_login.*
import javax.inject.Inject

class LoginActivity : BaseActivity(),LoginViewModel.UserLoginListener{

    @Inject
    lateinit var viewModel: LoginViewModel
    @Inject
    lateinit var preference: AppPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
        val binding : ActivityLoginBinding=DataBindingUtil.setContentView(this,R.layout.activity_login)
        binding.viewmodel=viewModel
        viewModel.userLoginListener=this
        binding.setLifecycleOwner(this)
        setupEvents()
        setupIntents()
    }

    private fun setupEvents(){
        viewModel.startActivityEvent.observe(this,object : StartActivityEvent.StartActivityObserver{
            override fun onStartActivityForResult(data: StartActivityModel) {
            startActivity(this@LoginActivity,Router.getClass(data.to),data.parameters,data.hasResults)
            }

            override fun onStartActivity(data: StartActivityModel) {
                startActivity(this@LoginActivity,Router.getClass(data.to),data.parameters,data.hasResults)
            }

        })

        viewModel.snackbarMessage.observe(this,object :SnackbarMessage.SnackbarObserver{
            override fun onNewMessage(message: String) {
                showSnackbar(et_password,message)
            }

            override fun onNewMessage(snackbarMessageResourceId: Int) {
                showSnackbar(et_password,getString(snackbarMessageResourceId))
            }

        })

        viewModel.tokenEvent.observe(this){
            preference.setAccessToken(it.toString())
            preference.setLoggedIn(true)
        }

        viewModel.loading.observe(this){
            showLoading(it)
        }
    }

    private fun setupIntents(){
        intent.getStringExtra(Router.Parameter.SNACKBAR_MSG.name)?.let {
            viewModel.snackbarMessage.postValue(it)
        }
    }


    override fun onFieldError(id: Int, msg: Int) {
        val view = findViewById<View>(id)
        when (view) {
            is AppCompatEditText -> {
                view.setError(if (msg == 0) "" else getString(msg))
//                scroll_view.post {
//                    scroll_view.smoothScrollTo(0, view.top)
//                }
            }
            is EditText -> {
                view.setError(if (msg == 0) "" else getString(msg))
//                scroll_view.post {
//                    scroll_view.smoothScrollTo(0, view.top)
//                }
            }
        }
    }
}
