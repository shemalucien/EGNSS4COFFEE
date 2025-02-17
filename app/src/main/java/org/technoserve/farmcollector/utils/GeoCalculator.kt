package org.technoserve.farmcollector.utils

import android.annotation.SuppressLint
import com.google.maps.android.SphericalUtil
import com.google.android.gms.maps.model.LatLng

object GeoCalculator {

    @SuppressLint("DefaultLocale")
    // The function to calculate the Area using the captured Polygons
    fun calculateArea(polygon: List<Pair<Double, Double>>?): Double {
        if (polygon == null || polygon.size <= 4) {
            return 0.0
        }
        // Convert the polygon vertices to LatLng objects expected by SphericalUtil
        val latLngList = polygon.map { LatLng(it.first, it.second) }

        // Calculate the area in square meters
        val areaInSquareMeters = SphericalUtil.computeArea(latLngList)

        // Convert area to hectares (1 hectare = 10,000 square meters)
        val areaInHectares = areaInSquareMeters / 10000.0

        // Format the result to 6 decimal places
        return String.format("%.6f", areaInHectares).toDouble()
    }
}
