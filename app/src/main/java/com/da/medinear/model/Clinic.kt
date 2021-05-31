package com.da.medinear.model

import java.io.Serializable
import kotlin.math.max

class Clinic : Serializable {
    var id: Long? = System.currentTimeMillis()
    var name: String? = null
    var open: Long? = null
    var close: Long? = null
    var avatar: String? = null
    var location: ClinicLocation? = null
    var phone: String? = null
    var rating: Map<String, Rating>? = null
    var website: String? = null

    fun getStarAvg() : Float {
        var star = 0f
        rating?.values?.forEach {
            star += it.star ?: 0f
        }
        return star / max(1, rating?.size ?: 0)
    }
}