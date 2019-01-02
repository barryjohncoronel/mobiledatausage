package com.barry.mobiledatausage

class MainActivityContract {

    interface View {
        fun setUpViews()
        fun showProgressDialog()
        fun hideProgressDialog()
        fun showError(message: String = "Something went wrong")
        fun showDataUsageList(annualDataUsage: MutableMap<String, Double>)
    }

    interface Presenter {
        fun callDataUsageApi()
    }

}