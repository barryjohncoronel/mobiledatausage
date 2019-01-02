package com.barry.mobiledatausage.api

import com.android.volley.VolleyError
import org.json.JSONObject

class ApiManager(serviceInterface: ApiServiceInterface) : ApiServiceInterface {
    private val service: ApiServiceInterface = serviceInterface

    override fun getDataUsage(completionHandler: (isSuccess: Boolean, error: VolleyError?, response: JSONObject?) -> Unit) {
        service.getDataUsage(completionHandler)
    }
}