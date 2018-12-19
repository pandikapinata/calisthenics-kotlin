package com.example.pandu.calisthenics.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Handler
import android.os.Looper
import android.support.v4.content.ContextCompat.getSystemService
import android.view.View
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.*








fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun calendarToRead(cal: Calendar, pattern: String): String {
    val dateFormat = SimpleDateFormat(pattern, Locale.ENGLISH)
    val now = Calendar.getInstance()
    val tommorow = Calendar.getInstance()
    tommorow.add(Calendar.DATE, +1)
    if (dateFormat.format(cal.time) == dateFormat.format(now.time))
        return "Today"
    else if (dateFormat.format(cal.time) == dateFormat.format(tommorow.time))
        return "Tommorow"
    return dateFormat.format(cal.time)
}

fun calendarToString(cal: Calendar, pattern: String): String {
    val dateFormat = SimpleDateFormat(pattern, Locale.ENGLISH)
    return dateFormat.format(cal.time)
}

fun toDatetoString(str: String, mode: String) : String {
    val format1 = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
    val format = SimpleDateFormat(mode, Locale.ENGLISH)
    val date = format1.parse(str)
    return format.format(date)
}

fun getDateSplit(str: String): String {
    val split = str.split(" ")
    val date = split[0]
    return date
}

fun getTimeSplit(str: String) : String {
    val split = str.split(" ")
    val time = split[1]
    return time
}

fun isNetworkAvailable(ctx: Context?): Boolean {
    val connectivityManager = ctx?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
    return if (connectivityManager is ConnectivityManager) {
        val networkInfo = connectivityManager.activeNetworkInfo
        networkInfo?.isConnected ?: false
    } else {
        false
    }
}

fun after(delay: Long, process: () -> Unit) {
    Handler().postDelayed({
        process()
    }, delay)
}

