package com.example.pandu.calisthenics.menu.profile

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import com.example.pandu.calisthenics.R
import com.example.pandu.calisthenics.api.APIClient
import com.example.pandu.calisthenics.api.ApiInterface
import com.example.pandu.calisthenics.model.User
import com.example.pandu.calisthenics.utils.PreferenceHelper
import com.example.pandu.calisthenics.utils.gone
import com.example.pandu.calisthenics.utils.visible
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment:Fragment(), ProfileView {
    private lateinit var progressBar: ProgressBar
    private var preferencesHelper: PreferenceHelper? = null
    private var presenter: ProfilePresenter? = null
    var rootView : View? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        rootView = inflater.inflate(R.layout.fragment_profile, container, false)
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        progressBar = pb_detail
        presenter?.showProfile()
        presenter = ProfilePresenter(this, APIClient.getService(activity))

        preferencesHelper = PreferenceHelper(activity)
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

        hideLoading()
    }

    override fun onError() {
        Toast.makeText(activity, "Error", Toast.LENGTH_SHORT).show()
    }

    override fun onFailure(t: Throwable) {
        Toast.makeText(activity, "Failed : $t", Toast.LENGTH_SHORT).show()
    }

}