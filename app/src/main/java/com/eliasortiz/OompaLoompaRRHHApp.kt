package com.eliasortiz

import android.app.Application
import com.eliasortiz.oompaloomparrhh.BuildConfig
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import timber.log.Timber

class OompaLoompaRRHHApp : Application() {

    override fun onCreate() {
        super.onCreate()

        //Logger
        if (BuildConfig.DEBUG) {
            Logger.addLogAdapter(AndroidLogAdapter())
            Timber.plant(object : Timber.DebugTree() {
                override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
                    Logger.log(priority, tag, message, t)
                }
            })
        }
    }
}