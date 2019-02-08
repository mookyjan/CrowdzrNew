package com.example.crowdzrnew.feature.login


import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.AppCompatEditText
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import com.example.crowdzrnew.AppPreference
import com.example.crowdzrnew.R
import com.example.crowdzrnew.core.BaseActivity
import com.example.crowdzrnew.core.Router
import com.example.crowdzrnew.core.event.SnackbarMessage
import com.example.crowdzrnew.core.event.StartActivityEvent
import com.example.crowdzrnew.core.event.StartActivityModel
import com.example.crowdzrnew.databinding.ActivityLoginBinding
import com.example.crowdzrnew.utilities.KeyboardManager.Companion.hideKeyboard
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
        setupActions()
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

    private fun setupActions() {
        // et_password.getEditText().setOnEditorActionListener(TextView.OnEditorActionListener(this::onKeyboardAction))

        et_email.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                checkIsEmpty(et_email)
                if (s != null) {
                    viewModel.username.set(s.toString())
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkIsEmpty(et_email)
            }

        })
        et_password.setOnEditorActionListener(TextView.OnEditorActionListener(this::onKeyboardAction))

        checkIsEmpty(et_password)
        et_password.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable?) {
                checkIsEmpty(et_password)
                if (s != null) {
                    viewModel.password.set(s.toString())
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkIsEmpty(et_password)
            }

        })
    }


    private fun checkIsEmpty(view: EditText) {
        if (view.text!!.isEmpty()) {
            view.typeface = ResourcesCompat.getFont(this@LoginActivity, R.font.open_sans_italic)
        } else {
            view.typeface = ResourcesCompat.getFont(this@LoginActivity, R.font.open_sans_bold)
        }
    }

    private fun onKeyboardAction(view: TextView, actionId: Int, keyEvent: KeyEvent?): Boolean {
        if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_NULL && keyEvent?.action == KeyEvent.ACTION_DOWN) {
            onDone(et_password)

        }
        return true
    }

    fun onDone(view: EditText) {
        hideKeyboard(this)
        if (viewModel.isFormValid.get()) {
            viewModel.onLoginClicked()
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
