package com.example.pandu.calisthenics.auth


import android.content.ContentValues
import android.util.Log
import com.example.pandu.calisthenics.api.ApiInterface
import com.example.pandu.calisthenics.model.AuthResponse
import com.example.pandu.calisthenics.model.BasicResponse
import com.example.pandu.calisthenics.model.TaskResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subscribers.ResourceSubscriber
import okhttp3.RequestBody
import retrofit2.Callback
import retrofit2.Response


class AuthPresenter(private val view: AuthView, private val service: ApiInterface) {

    fun login(email: String, password: String) {
//        view.showLoading()
        service.loginUser(email, password)
            .enqueue(object : Callback<AuthResponse>{
                override fun onResponse(call: retrofit2.Call<AuthResponse>, response: Response<AuthResponse>) {
                    if (response.isSuccessful) {
                        view.onSuccess(response.body()!!)
                    } else {
                        view.onError()
                    }
                    view.hideLoading()
                }

                override fun onFailure(call: retrofit2.Call<AuthResponse>, t: Throwable) {
                    view.onFailure(t)
                    view.hideLoading()
                }
            })
    }

    fun register(name: String, email: String, password: String) {
        view.showLoading()
        service.registerUser(name, email, password)
            .enqueue(object : Callback<AuthResponse>{
                override fun onResponse(call: retrofit2.Call<AuthResponse>, response: Response<AuthResponse>) {
                    if (response.isSuccessful) {
                        view.onSuccess(response.body()!!)

                    } else {
                        view.onError()
                    }
                    view.hideLoading()
                }

                override fun onFailure(call: retrofit2.Call<AuthResponse>, t: Throwable) {
                    view.onFailure(t)
                    view.hideLoading()
                }
            })
    }

//    private val compositeDisposable = CompositeDisposable()
//    fun loadTasks() {
//        view.showLoading()
//        val disposable : Disposable
//        disposable = service.loadTasks()
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribeOn(Schedulers.io())
//            .subscribeWith(object : ResourceSubscriber<TaskResponse>(){
//                override fun onComplete() {
//
//                }
//
//                override fun onNext(t: TaskResponse?) {
//                    t?.tasks?.let { view.getTaskList(it) }
//
//                }
//
//                override fun onError(t: Throwable?) {
//                    Log.d(ContentValues.TAG, "ERROR LOAD TASKS$t")
//                }
//
//            })
//        compositeDisposable.addAll(disposable)
//    }



}