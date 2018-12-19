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
import com.example.pandu.calisthenics.utils.after
import org.jetbrains.anko.indeterminateProgressDialog
import org.jetbrains.anko.startActivity
import android.app.Activity
import android.content.ContentValues
import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import com.example.pandu.calisthenics.db.database
import com.example.pandu.calisthenics.model.Task
import com.example.pandu.calisthenics.model.TaskResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subscribers.ResourceSubscriber
import okhttp3.RequestBody
import retrofit2.Callback
import retrofit2.Response
import org.jetbrains.anko.db.*


@Suppress("DEPRECATION")
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

    override fun getTaskList(task: List<Task>) {
//    insert to sqlite
        try{
            database.use{
                for(value in task){
                    insert(Task.TABLE_TASK,
                        Task.TASK_ID to value.taskId,
                        Task.ID_ACTIVITY to value.activityId,
                        Task.TASK_NAME to value.taskName,
                        Task.TASK_NOTE to value.taskNote,
                        Task.TASK_SETS to value.taskSets,
                        Task.TASK_REPS to value.taskReps,
                        Task.TASK_VOLUME to value.taskVolume,
                        Task.TASK_DATE to value.taskDate,
                        Task.TASK_ICON to value.taskIcon,
                        Task.STATUS_PUSH to "1",
                        Task.STATUS_DELETE to "1"
                    )
                }
            }

        } catch (e: SQLiteConstraintException){
            Toast.makeText(this@LoginActivity, e.localizedMessage, Toast.LENGTH_SHORT).show()
        }
    }

    private fun submitForm() {
        if (!validateEmail()) {
            return
        }

        if (!validatePassword()) {
            return
        }
        showLoading()
        presenter = AuthPresenter(this, APIClient.getService(this))

        presenter?.login(
                email_edit_text.text.toString(),
                password_edit_text.text.toString())

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
        indeterminateProgressDialog("Please wait a bit…").show()
    }

    override fun hideLoading() {
        indeterminateProgressDialog("Please wait a bit…").dismiss()

    }

    override fun onSuccess(userAuth: AuthResponse) {
        preferencesHelper?.setUserLogin(userAuth)

        pushFCMToken()
        loadDataServer()
        after(2000){
            startActivity<MainActivity>()
            finish()
        }
        hideLoading()
    }

    override fun onError() {
        Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
    }

    override fun onFailure(t: Throwable) {
        Toast.makeText(this, "Failed : $t", Toast.LENGTH_SHORT).show()
    }

    private fun pushFCMToken(){
        val pref = PreferenceHelper(this)
        val apiToken = pref.deviceToken
        //check apiToken already in there
        Log.i("TOKEN_AUTH", "$apiToken")
        val fcmUSER = pref.getFCM
        Log.i("FCM_TOKEN", fcmUSER)
        val fcmToken = RequestBody.create(okhttp3.MultipartBody.FORM, fcmUSER)
        APIClient.getService(this)
            .pushToken(fcmToken)
            .enqueue(object : Callback<AuthResponse> {
                override fun onResponse(call: retrofit2.Call<AuthResponse>, response: Response<AuthResponse>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@LoginActivity, "Push FCM Token", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this@LoginActivity, "Failed", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: retrofit2.Call<AuthResponse>, t: Throwable) {
                    Toast.makeText(this@LoginActivity, "Error: $t", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun loadDataServer(){
        val compositeDisposable = CompositeDisposable()
        val pref = PreferenceHelper(this)
        val apiToken = pref.deviceToken
        //check apiToken already in there
        Log.i("TOKEN_AUTH", "$apiToken")

        val service = APIClient.getService(this)
            val disposable : Disposable
            disposable = service.loadTasks()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeWith(object : ResourceSubscriber<TaskResponse>(){
                    override fun onComplete() {

                    }

                    override fun onNext(t: TaskResponse?) {
                        t?.tasks?.let { getTaskList(it) }
                        Toast.makeText(this@LoginActivity, "Load data from server success", Toast.LENGTH_LONG).show()
                    }

                    override fun onError(t: Throwable?) {
                        Log.d(ContentValues.TAG, "ERROR LOAD TASKS$t")
                    }

                })
            compositeDisposable.addAll(disposable)
    }

}
