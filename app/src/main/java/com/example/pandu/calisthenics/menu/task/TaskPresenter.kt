package com.example.pandu.calisthenics.menu.task

import android.content.ContentValues
import android.util.Log
import com.example.pandu.calisthenics.api.ApiInterface
import com.example.pandu.calisthenics.model.ActivityResponse
import com.example.pandu.calisthenics.model.TaskResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subscribers.ResourceSubscriber


class TaskPresenter(private val view: TaskView, private val service: ApiInterface) {
    val compositeDisposable = CompositeDisposable()
    fun getTask() {
        view.showLoading()
        val disposable : Disposable
        disposable = service.getTaskUser()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribeWith(object : ResourceSubscriber<TaskResponse>(){
                override fun onComplete() {
                    view.hideLoading()
                }

                override fun onNext(t: TaskResponse?) {
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



    fun getActivities() {
        view.showLoading()
        val disposable : Disposable
        disposable = service.getActivities()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribeWith(object : ResourceSubscriber<ActivityResponse>(){
                override fun onComplete() {
                    view.hideLoading()
                }

                override fun onNext(t: ActivityResponse?) {
                    t?.listActivities?.let { view.showActivityList(it) }
                    view.hideLoading()
                }

                override fun onError(t: Throwable?) {
                    Log.d(ContentValues.TAG, "ERROR GET ACTIVITY$t")
                    view.getLocalData()
                    view.hideLoading()
                }

            })
        compositeDisposable.addAll(disposable)
    }

    fun storeTask(activityId: String, note: String, sets: String, reps: String, volume:String, dateTask: String) {
        view.showLoading()
        val disposable : Disposable
        disposable = service.storeTask(activityId, note, sets, reps, volume, dateTask)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribeWith(object : ResourceSubscriber<TaskResponse>(){
                override fun onComplete() {
                    view.hideLoading()
                }

                override fun onNext(t: TaskResponse?) {
                    t?.tasks?.let {
                        view.storingTask()
                    }
                    view.hideLoading()

                }

                override fun onError(t: Throwable?) {
                    Log.d(ContentValues.TAG, "ERROR STORING TASKS$t")
                    view.hideLoading()
                }

            })
        compositeDisposable.addAll(disposable)
    }


}