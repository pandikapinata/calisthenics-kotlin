package com.example.pandu.calisthenics.auth

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_login.*
import android.view.WindowManager
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import android.text.Editable
import android.text.TextWatcher
import com.example.pandu.calisthenics.MainActivity
import com.example.pandu.calisthenics.R
import com.example.pandu.calisthenics.api.APIClient
import com.example.pandu.calisthenics.model.AuthResponse
import com.example.pandu.calisthenics.utils.PreferenceHelper
import org.jetbrains.anko.startActivity

class LoginActivity : AppCompatActivity(), AuthView {

    private var preferencesHelper: PreferenceHelper? = null
    private var presenter: AuthPresenter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        preferencesHelper = PreferenceHelper(this)
        val token = preferencesHelper?.deviceToken

        if(token != ""){
            startActivity<MainActivity>()
            finish()
        }else{
            email_edit_text.addTextChangedListener(MyTextWatcher(email_edit_text))
            password_edit_text.addTextChangedListener(MyTextWatcher(password_edit_text))

            tv_sign_up?.setOnClickListener {
                startActivity<RegisterActivity>()
            }

            btn_login.setOnClickListener {
                submitForm()
            }
        }

    }


    private fun submitForm() {
        if (!validateEmail()) {
            return
        }

        if (!validatePassword()) {
            return
        }
        presenter?.login(
                email_edit_text.text.toString(),
                password_edit_text.text.toString())
        presenter = AuthPresenter(this, APIClient.getService(this))

    }

    private fun validateEmail(): Boolean {
        val email = email_edit_text.text.toString().trim()

        if (email.isEmpty() || !isValidEmail(email)) {
            email_text_input.error = getString(R.string.err_msg_email)
            requestFocus(email_edit_text)
            return false
        } else {
            email_text_input.isErrorEnabled = false
        }

        return true
    }

    private fun validatePassword(): Boolean {
        if (password_edit_text.text.toString().trim().isEmpty()) {
            password_text_input.error = getString(R.string.err_msg_password)
            requestFocus(password_edit_text)
            return false
        } else {
            password_text_input.isErrorEnabled = false
        }

        return true
    }

    private fun isValidEmail(email: String): Boolean {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun requestFocus(view: View) {
        if (view.requestFocus()) {
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
        }
    }

    private inner class MyTextWatcher internal constructor(private val view: View) : TextWatcher {

        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

        override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

        override fun afterTextChanged(editable: Editable) {
            when (view.id) {
                R.id.email_edit_text -> validateEmail()
                R.id.password_edit_text -> validatePassword()
            }
        }
    }

    override fun showLoading() {
        Toast.makeText(this, "Loading", Toast.LENGTH_SHORT).show()
    }

    override fun hideLoading() {
        Toast.makeText(this, "Loaded", Toast.LENGTH_SHORT).show()
    }

    override fun onSuccess(userAuth: AuthResponse) {
        preferencesHelper?.setUserLogin(userAuth)

        Toast.makeText(this, userAuth.access_token, Toast.LENGTH_SHORT).show()
        startActivity<MainActivity>()
        finish()
    }

    override fun onError() {
        Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
    }

    override fun onFailure(t: Throwable) {
        Toast.makeText(this, "Failed : $t", Toast.LENGTH_SHORT).show()
    }

}
