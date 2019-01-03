package com.barry.mobiledatausage

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
                val qduList: List<Model.QuarterlyDataUsage>

                try {
                    qduList = Gson().fromJson<List<Model.QuarterlyDataUsage>>(
                        response?.getJSONObject("result")?.getJSONArray("records").toString(),
                        listType
                    )

                    parseResponse(qduList)
                } catch (je: JSONException) {
                    mainView.showError(je.localizedMessage)
                }
            } else {
                mainView.showError()
            }
        }
    }

    private fun parseResponse(quarterlyDataUsageRecords: List<Model.QuarterlyDataUsage>) {
        val aduList = mutableMapOf<String, Model.AnnualDataUsage>()
        var tempVolume = 0.00
        var tempYear = 0

        quarterlyDataUsageRecords.forEach { it ->
            val year = it.year.substring(0, 4)

            if (aduList.containsKey(year)) {
                if (it.volume.toDouble() < tempVolume) {
                    tempYear = year.toInt()
                    tempVolume = it.volume.toDouble()
                }

                aduList[year] = Model.AnnualDataUsage(
                    year,
                    aduList.getValue(year).volume + it.volume.toDouble(),
                    tempYear == year.toInt()
                )
            } else {
                tempVolume = it.volume.toDouble()
                aduList[year] = Model.AnnualDataUsage(year, it.volume.toDouble())
            }
        }

        mainView.showDataUsageList(aduList)
    }
}