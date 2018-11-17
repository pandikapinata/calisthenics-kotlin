package com.example.pandu.calisthenics.menu

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pandu.calisthenics.R
import com.example.pandu.calisthenics.utils.PreferenceHelper
import kotlinx.android.synthetic.main.fragment_dashboard.*

class DashboradFragment: Fragment() {
    var rootView : View? = null
    private var preferencesHelper: PreferenceHelper? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        rootView = inflater.inflate(R.layout.fragment_dashboard, container, false)
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        preferencesHelper = PreferenceHelper(activity)
        val token = preferencesHelper?.deviceToken
        tv_token.text = token
    }
}