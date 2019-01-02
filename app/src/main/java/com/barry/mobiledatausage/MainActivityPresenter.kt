package com.barry.mobiledatausage

import android.util.Log
import com.barry.mobiledatausage.api.ApiManager
import com.barry.mobiledatausage.api.ApiService
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONException

class MainActivityPresenter(private val mainView: MainActivityContract.View) : MainActivityContract.Presenter {

    override fun callDataUsageApi() {
        val apiService = ApiService()
        val apiManager = ApiManager(apiService)

        mainView.showProgressDialog()
        apiManager.getDataUsage { isSuccess, error, response ->
            mainView.hideProgressDialog()

            if (isSuccess && error == null) {
                val listType = object : TypeToken<List<Model.QuarterlyDataUsage>>() {}.type
                val quarterlyDataUsageRecords: List<Model.QuarterlyDataUsage>

                try {
                    quarterlyDataUsageRecords = Gson().fromJson<List<Model.QuarterlyDataUsage>>(
                        response?.getJSONObject("result")?.getJSONArray("records").toString(),
                        listType
                    )

                    map(quarterlyDataUsageRecords)
                } catch (je: JSONException) {
                    mainView.showError("Oops! -> ${je.localizedMessage}")
                }
            } else {
                mainView.showError()
            }
        }
    }

    private fun map(quarterlyDataUsageRecords: List<Model.QuarterlyDataUsage>) {
        val annualDataUsage = mutableMapOf<String, Double>()
        var tempVolume = 0.00

        quarterlyDataUsageRecords.forEach { it ->
            val year = it.year.substring(0, 4)

            Log.d("fak", "year: ${it.year} volume: ${it.volume.toDouble()} tempVolume: $tempVolume")

            if (annualDataUsage.containsKey(year)) {
                if (it.volume.toDouble() < tempVolume) {
                    //TODO: maglagay ng indicator

                    Log.d("bars", "year: $year")
                    tempVolume = it.volume.toDouble()
                }
                annualDataUsage[year] = annualDataUsage.getValue(year).toDouble() + it.volume.toDouble()
            } else {
                tempVolume = it.volume.toDouble()
                annualDataUsage[year] = it.volume.toDouble()
            }
        }

        mainView.showDataUsageList(annualDataUsage)
    }
}