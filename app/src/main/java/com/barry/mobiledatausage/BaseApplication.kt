package com.barry.mobiledatausage

import android.app.Application
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    private val requestQueue: RequestQueue? = null
        get() {
            if (field == null) {
                return Volley.newRequestQueue(applicationContext)
            }
            return field
        }

    fun <T> addToRequestQueue(request: Request<T>, tag: String) {
        request.tag = tag
        requestQueue?.add(request)
    }

    companion object {
        @get:Synchronized
        var instance: BaseApplication? = null
            private set
    }
}