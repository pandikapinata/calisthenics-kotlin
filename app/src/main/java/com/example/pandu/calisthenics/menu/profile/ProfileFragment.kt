package com.example.pandu.calisthenics.menu.profile

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import com.example.pandu.calisthenics.R
import com.example.pandu.calisthenics.api.APIClient
import com.example.pandu.calisthenics.auth.LoginActivity
import com.example.pandu.calisthenics.model.User
import com.example.pandu.calisthenics.utils.PreferenceHelper
import com.example.pandu.calisthenics.utils.gone
import com.example.pandu.calisthenics.utils.visible
import kotlinx.android.synthetic.main.fragment_profile.*
import org.jetbrains.anko.startActivity





class ProfileFragment:Fragment(), ProfileView {
    private lateinit var progressBar: ProgressBar
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private var preferencesHelper: PreferenceHelper? = null
    private var presenter: ProfilePresenter? = null
    var rootView : View? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        rootView = inflater.inflate(R.layout.fragment_profile, container, false)
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        swipeRefresh = sr_profile
        progressBar = pb_profile
        preferencesHelper = PreferenceHelper(activity)
        presenter = ProfilePresenter(this, APIClient.getService(activity))
        presenter?.showProfile()
        sr_profile.setOnRefreshListener {
            presenter?.showProfile()
        }

        tv_logout.setOnClickListener {
            preferencesHelper?.logout()
            val apiToken = preferencesHelper?.deviceToken
            //check apiToken already in there

            val intent = Intent(context, LoginActivity::class.java)
            activity!!.startActivity(intent)
            activity!!.finish()
            Log.i("apiToken", "$apiToken")
//            startActivity<LoginActivity>()

        }

    }

    override fun showLoading() {
        progressBar.visible()
    }

    override fun hideLoading() {
        progressBar.gone()
    }

    override fun getProfileUser(user: User) {

        tv_name_user.text = user.name
        tv_email_user.text = user.email
        sr_profile.isRefreshing = false
        hideLoading()
    }

    override fun onError() {
        Toast.makeText(activity, "Error", Toast.LENGTH_SHORT).show()
    }

    override fun onFailure(t: Throwable) {
        Toast.makeText(activity, "Failed : $t", Toast.LENGTH_SHORT).show()
    }

}