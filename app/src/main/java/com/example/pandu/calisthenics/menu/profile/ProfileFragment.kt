package com.example.pandu.calisthenics.menu.profile

import android.database.sqlite.SQLiteConstraintException
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.*
import android.widget.ProgressBar
import android.widget.Toast
import com.example.pandu.calisthenics.R
import com.example.pandu.calisthenics.api.APIClient
import com.example.pandu.calisthenics.auth.LoginActivity
import com.example.pandu.calisthenics.model.User
import kotlinx.android.synthetic.main.fragment_profile.*
import org.jetbrains.anko.startActivity
import android.view.MenuInflater
import com.bumptech.glide.Glide
import com.example.pandu.calisthenics.db.database
import com.example.pandu.calisthenics.model.Task
import com.example.pandu.calisthenics.model.TaskDay
import com.example.pandu.calisthenics.utils.*
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select
import org.jetbrains.anko.design.snackbar


class ProfileFragment : Fragment(), ProfileView {

    private lateinit var progressBar: ProgressBar
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private var preferencesHelper: PreferenceHelper? = null
    private var presenter: ProfilePresenter? = null
    var rootView: View? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_profile, container, false)
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).supportActionBar!!.title = "Profile"
        swipeRefresh = sr_profile
        progressBar = pb_profile
        preferencesHelper = PreferenceHelper(context)
        presenter = ProfilePresenter(this, APIClient.getService(context))


        if (isNetworkAvailable(context)) {
            presenter?.showProfile()
            sr_profile.setOnRefreshListener {
                sr_profile.isRefreshing = false
                presenter?.showProfile()
            }
        } else {
            getLocalData()
            sr_profile.setOnRefreshListener {
                sr_profile.isRefreshing = false
                getLocalData()
            }
            hideLoading()
        }


    }

    override fun showLoading() {
        progressBar.visible()
    }

    override fun hideLoading() {
        progressBar.gone()
    }

    override fun getProfileUser(user: User) {

        context?.database?.use {
            delete(User.TABLE_USER)
        }
        //insert to sqlite
        try {
            context?.database?.use {
                insert(
                    User.TABLE_USER,
                    User.USER_ID to user.idUser,
                    User.NAME_USER to user.name,
                    User.EMAIL_USER to user.email,
                    User.FCM_TOKEN_USER to user.fcm_token,
                    User.WEIGHT_USER to user.weight,
                    User.HEIGHT_USER to user.height,
                    User.PHOTO_PROFILE to user.photo_profile
                )
            }

        } catch (e: SQLiteConstraintException) {
            Toast.makeText(context, e.localizedMessage, Toast.LENGTH_SHORT).show()
        }
        getLocalData()
        hideLoading()
    }

    override fun onError() {
        Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
    }

    override fun onFailure(t: Throwable) {
        Toast.makeText(context, "Failed : $t", Toast.LENGTH_SHORT).show()
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_logout, menu)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout_profile -> {
                preferencesHelper?.logout()
                context?.database?.use {
                    delete(User.TABLE_USER)
                    delete(Task.TABLE_TASK)
                    delete(TaskDay.TABLE_TASKDAY)
                }
                context?.startActivity<LoginActivity>()
                true

            }
            R.id.edit_profile -> {
                context?.startActivity<EditProfileActivity>()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun getLocalData() {

        context?.database?.use {
            val result = select(User.TABLE_USER)
            Log.e("USER", result.toString())
            val taskSQLite = result.parseOpt(classParser<User>())

            tv_name_user.text = taskSQLite?.name
            tv_email_user.text = taskSQLite?.email
            et_weight.setText(taskSQLite?.weight)
            et_height.setText(taskSQLite?.height)
            Glide.with(this@ProfileFragment)
                .load(taskSQLite?.photo_profile)
                .into(profile_image)

            val bmiScore = ("%.2f".format(bmiScore(taskSQLite?.weight.toString(), taskSQLite?.height.toString())))
            et_bmi_score.setText(bmiScore)
            val bmiStatus = bmiScore.toDouble()
            et_bmi_status.setText(bmiStatus(bmiStatus))
        }

    }

    private fun bmiScore(weight: String, height: String): Double {
        val weightInt = weight.toInt()
        val heightInt:Double = height.toInt()/100.toDouble()
        Log.e("HEIGHT", heightInt.toString())
        val bmi = weightInt/(heightInt*heightInt)
        return bmi
    }

    private fun bmiStatus(bmi: Double): String {
        return if (bmi <17.0 || bmi<18.4)
            "Kurus"
        else if (bmi > 18.5 || bmi<25)
            "Normal"
        else if (bmi > 25.1)
            "Gemuk"
        else
            " "
    }

    override fun onResume() {
        super.onResume()
        presenter?.showProfile()
    }

}