package com.example.pandu.calisthenics.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.pandu.calisthenics.model.ActivityItem
import com.example.pandu.calisthenics.model.Task
import com.example.pandu.calisthenics.model.TaskDay
import com.example.pandu.calisthenics.model.User
import org.jetbrains.anko.db.*

class SQLiteOpenHelper(ctx:Context): ManagedSQLiteOpenHelper(ctx, "TaskScheduler.db", null, 1) {
    companion object {
        private var instance: SQLiteOpenHelper? = null

        @Synchronized
        fun getInstance(ctx: Context): SQLiteOpenHelper {
            if (instance == null) {
                instance = SQLiteOpenHelper(ctx.applicationContext)
            }
            return instance as SQLiteOpenHelper
        }
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.createTable(
            Task.TABLE_TASK, true,
            Task.ID to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
//            Task.TASK_ID to TEXT,
//            Task.USER_ID to TEXT,
            Task.ID_ACTIVITY to TEXT,
            Task.TASK_NAME to TEXT,
            Task.TASK_NOTE to TEXT,
            Task.TASK_SETS to TEXT,
            Task.TASK_REPS to TEXT,
            Task.TASK_VOLUME to TEXT,
            Task.TASK_DATE to TEXT,
            Task.TASK_ICON to TEXT,
            Task.STATUS to TEXT
        )

        db?.createTable(
            TaskDay.TABLE_TASKDAY, true,
            TaskDay.ID to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
            TaskDay.TASK_ID to TEXT,
            TaskDay.USER_ID to TEXT,
            TaskDay.ID_ACTIVITY to TEXT,
            TaskDay.TASK_NAME to TEXT,
            TaskDay.TASK_NOTE to TEXT,
            TaskDay.TASK_SETS to TEXT,
            TaskDay.TASK_REPS to TEXT,
            TaskDay.TASK_VOLUME to TEXT,
            TaskDay.TASK_DATE to TEXT,
            TaskDay.TASK_ICON to TEXT
        )

        db?.createTable(
            User.TABLE_USER, true,
            User.ID to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
            User.USER_ID to TEXT + UNIQUE,
            User.NAME_USER to TEXT,
            User.EMAIL_USER to TEXT,
            User.FCM_TOKEN_USER to TEXT,
            User.WEIGHT_USER to TEXT,
            User.HEIGHT_USER to TEXT,
            User.PHOTO_PROFILE to TEXT
        )

        db?.createTable(
            ActivityItem.TABLE_ACTIVITY, true,
            ActivityItem.ID to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
            ActivityItem.ACTIVITY_ID to TEXT + UNIQUE,
            ActivityItem.ACTIVITY_NAME to TEXT,
            ActivityItem.ACTIVITY_ICON to TEXT
        )
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.dropTable(Task.TABLE_TASK, true)
        db?.dropTable(User.TABLE_USER, true)
        db?.dropTable(TaskDay.TABLE_TASKDAY, true)
        db?.dropTable(ActivityItem.TABLE_ACTIVITY, true)
    }

}

// Access property for Context
val Context.database: SQLiteOpenHelper
    get() = SQLiteOpenHelper.getInstance(applicationContext)