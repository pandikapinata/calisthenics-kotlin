package com.example.pandu.calisthenics.menu.dashboard

import android.content.ContentValues
import android.util.Log
import com.example.pandu.calisthenics.api.ApiInterface
import com.example.pandu.calisthenics.model.StatsResponse
import com.example.pandu.calisthenics.model.TaskDayResponse
import com.example.pandu.calisthenics.model.TaskResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subscribers.ResourceSubscriber
import retrofit2.Callback
import retrofit2.Response

class DashboardPresenter(private val view: DashboardView, private val service: ApiInterface) {
    val compositeDisposable = CompositeDisposable()
    fun getTaskperDay() {
        view.showLoading()
        val disposable : Disposable
        disposable = service.getTaskUserperDay()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribeWith(object : ResourceSubscriber<TaskDayResponse>(){
                override fun onComplete() {
                    view.hideLoading()
                }

                override fun onNext(t: TaskDayResponse?) {
                    t?.tasks?.let { view.showTaskList(it) }
                    view.hideLoading()
                }

                override fun onError(t: Throwable?) {
                    Log.d(ContentValues.TAG, "ERROR GET TASKS$t")
                    view.getLocalData()
                    view.hideLoading()
                }

            })
        compositeDisposable.addAll(disposable)
    }

    fun getStatsperDay() {
        view.showLoading()
        service.getStatsDay()
            .enqueue(object : Callback<StatsResponse> {
                override fun onResponse(call: retrofit2.Call<StatsResponse>, response: Response<StatsResponse>) {
                    if (response.isSuccessful) {
                        view.getStatsData(response.body()!!)
                    } else {

                    }
                    view.hideLoading()
                }

                override fun onFailure(call: retrofit2.Call<StatsResponse>, t: Throwable) {
                    view.hideLoading()
                }
            })
    }
}