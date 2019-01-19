package com.barry.mobiledatausage

import com.barry.mobiledatausage.api.ApiInterface
import org.json.JSONException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivityPresenter(private val mainView: MainActivityContract.View) : MainActivityContract.Presenter {

    override fun callDataUsageApi() {
        val resourceId = "a807b7ab-6cad-4aa6-87d0-e283a7353a0f"

        mainView.showProgressDialog()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://data.gov.sg/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiInterface = retrofit.create<ApiInterface>(ApiInterface::class.java)

        val call = apiInterface.getPosts(resourceId)
        call.enqueue(object : Callback<Model.DataUsageResponse> {
            override fun onResponse(call: Call<Model.DataUsageResponse>, response: Response<Model.DataUsageResponse>) {
                mainView.hideProgressDialog()

                try {
                    val quarterlyDataUsageRecords = response.body()!!.result.quarterlyDataUsage

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
                } catch (je: JSONException) {
                    mainView.showError("Something went wrong!")
                }
            }

            override fun onFailure(call: Call<Model.DataUsageResponse>, t: Throwable) {
                mainView.hideProgressDialog()
                mainView.showError("Something went wrong!")
            }
        })
    }
}