package com.barry.mobiledatausage

import android.support.annotation.Keep
import com.google.gson.annotations.SerializedName

class Model {

    @Keep
    data class AnnualDataUsage(var year: String, var volume: Double, val hasDecreasedUsage: Boolean = false)

    @Keep
    data class QuarterlyDataUsage(
        @SerializedName("quarter") val year: String,
        @SerializedName("volume_of_mobile_data") val volume: String
    )
}