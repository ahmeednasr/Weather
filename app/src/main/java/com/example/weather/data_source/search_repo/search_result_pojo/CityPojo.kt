package com.example.weather.data_source.search_repo.search_result_pojo

import androidx.room.Entity
import androidx.room.Ignore
import java.io.Serializable

@Entity(
    tableName = "Cities",
    primaryKeys = ["lat", "lon"]
)
data class CityPojo(
    val country: String,
    val lat: Double,
    val lon: Double,
    val name: String,
    var state: String = "",
) : Serializable {
    constructor(
        country: String,
        lat: Double,
        lon: Double,
        name: String,
        state: String,
        local_names: LocalNames
    ) : this(country, lat, lon, name, state) {
        // Initialize the local_names field with the provided parameter.
        this.local_names = local_names
    }

    // Declare the local_names property as a var, since it will be initialized later.
    @Ignore
    lateinit var local_names: LocalNames
}