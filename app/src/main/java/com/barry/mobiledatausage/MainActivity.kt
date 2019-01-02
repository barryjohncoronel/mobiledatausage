package com.barry.mobiledatausage

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Handler
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.row_item_annual_data_usage.view.*
import java.text.DecimalFormat

class MainActivity : AppCompatActivity(), MainActivityContract.View {

    val TAG = MainActivity::class.java.simpleName

    private lateinit var presenter: MainActivityPresenter
    private var listAdapter = GroupAdapter<ViewHolder>()
    var hasDecreaseUsage = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(R.layout.activity_main)

        presenter = MainActivityPresenter(this@MainActivity)

        setUpViews()
    }

    override fun setUpViews() {
        rv_data_usage.apply {
            adapter = listAdapter
        }

        refresh_layout.setColorSchemeColors(ContextCompat.getColor(this@MainActivity, R.color.colorAccent))
        refresh_layout.setOnRefreshListener {
            presenter.callDataUsageApi()
        }

        presenter.callDataUsageApi()
    }

    override fun showProgressDialog() {
        refresh_layout.isRefreshing = true
    }

    override fun hideProgressDialog() {

        Handler().postDelayed({ refresh_layout.isRefreshing = false }, 1000)
    }

    override fun showError(message: String) {
        Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
    }

    override fun showDataUsageList(annualDataUsage: MutableMap<String, Double>) {
        listAdapter.clear()

        annualDataUsage.forEach {
            listAdapter.add(DataUsageItem(Model.AnnualDataUsage(it.key, it.value)))
        }
    }

    private class DataUsageItem(val dataUsage: Model.AnnualDataUsage) : Item() {
        val decimalFormat = DecimalFormat("#.######")

        override fun bind(viewHolder: ViewHolder, position: Int) {
            viewHolder.itemView.apply {
                data_usage_text_year.text = dataUsage.year
                data_usage_volume.text = decimalFormat.format(dataUsage.volume)
            }
        }

        override fun getLayout() = R.layout.row_item_annual_data_usage
    }
}
