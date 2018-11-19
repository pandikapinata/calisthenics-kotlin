package com.example.pandu.calisthenics.auth

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.example.pandu.calisthenics.R
import com.example.pandu.calisthenics.api.APIClient
import com.example.pandu.calisthenics.model.AuthResponse
import com.example.pandu.calisthenics.utils.PreferenceHelper
import kotlinx.android.synthetic.main.activity_register.*
import org.jetbrains.anko.startActivity

class RegisterActivity : AppCompatActivity(), AuthView{

    private var preferencesHelper: PreferenceHelper? = null
    private var presenter: AuthPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        name_edit_text_regis.addTextChangedListener(MyTextWatcher(name_edit_text_regis))
        email_edit_text_regis.addTextChangedListener(MyTextWatcher(email_edit_text_regis))
        password_edit_text_regis.addTextChangedListener(MyTextWatcher(password_edit_text_regis))

        tv_login?.setOnClickListener {
            startActivity<LoginActivity>()
        }

        btn_register.setOnClickListener {
            submitForm()
        }
    }

    private fun submitForm() {
        if (!validateName()) {
            return
        }

        if (!validateEmail()) {
            return
        }

        if (!validatePassword()) {
            return
        }

        presenter?.register(
            name_edit_text_regis.text.toString(),
            email_edit_text_regis.text.toString(),
            password_edit_text_regis.text.toString())
        presenter = AuthPresenter(this, APIClient.getService(this))

    }


    private fun validateName(): Boolean {
        if (name_edit_text_regis.text.toString().trim().isEmpty()) {
            name_text_input_regis.error = getString(R.string.err_msg_name)
            requestFocus(name_edit_text_regis)
            return false
        } else {
            name_text_input_regis.isErrorEnabled = false
        }

        return true
    }

    private fun validateEmail(): Boolean {
        val email = email_edit_text_regis.text.toString().trim()

        if (email.isEmpty() || !isValidEmail(email)) {
            email_text_input_regis.error = getString(R.string.err_msg_email)
            requestFocus(email_edit_text_regis)
            return false
        } else {
            email_text_input_regis.isErrorEnabled = false
        }

        return true
    }

    private fun validatePassword(): Boolean {
        if (password_edit_text_regis.text.toString().trim().isEmpty()) {
            password_text_input_regis.error = getString(R.string.err_msg_password)
            requestFocus(password_edit_text_regis)
            return false
        } else {
            password_text_input_regis.isErrorEnabled = false
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
                R.id.name_edit_text_regis -> validateName()
                R.id.email_edit_text_regis -> validateEmail()
                R.id.password_edit_text_regis-> validatePassword()
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
        Toast.makeText(this, "Register Success", Toast.LENGTH_SHORT).show()
        startActivity<LoginActivity>()
        finish()
    }

    override fun onError() {
        Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
    }

    override fun onFailure(t: Throwable) {
        Toast.makeText(this, "Failed : $t", Toast.LENGTH_SHORT).show()
    }
}
