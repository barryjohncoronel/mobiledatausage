package com.barry.mobiledatausage.api

import android.util.Log
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.VolleyLog
import com.android.volley.toolbox.JsonObjectRequest
import com.barry.mobiledatausage.BaseApplication
import org.json.JSONObject

interface ApiServiceInterface {
    fun getDataUsage(completionHandler: (isSuccess: Boolean, error: VolleyError?, response: JSONObject?) -> Unit)
}

class ApiService : ApiServiceInterface {
    val TAG = ApiService::class.java.simpleName
    val url = "https://data.gov.sg/api/action/datastore_search?resource_id=a807b7ab-6cad-4aa6-87d0-e283a7353a0f"

    override fun getDataUsage(completionHandler: (isSuccess: Boolean, error: VolleyError?, response: JSONObject?) -> Unit) {
        val jsonObjReq = object : JsonObjectRequest(Method.GET, url, null,
            Response.Listener<JSONObject> { response ->
                Log.d(TAG, "response: $response")

                completionHandler(true, null, response)
            },
            Response.ErrorListener { error ->
                VolleyLog.e(TAG, "response error: ${error.message}")

                completionHandler(false, error, null)
            }) {}

        BaseApplication.instance?.addToRequestQueue(jsonObjReq, TAG)
    }

}