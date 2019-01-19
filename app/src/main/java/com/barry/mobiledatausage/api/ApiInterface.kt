package com.barry.mobiledatausage.api

import com.barry.mobiledatausage.Model
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

private const val DATA_USAGE = "api/action/datastore_search" //key
private const val RESOURCE_ID = "resource_id" //path

interface ApiInterface {
    @GET(DATA_USAGE)
    fun getPosts(@Query(RESOURCE_ID) resourceId: String): Call<Model.DataUsageResponse>
}