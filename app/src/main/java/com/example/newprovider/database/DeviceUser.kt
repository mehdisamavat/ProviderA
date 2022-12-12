package com.example.newprovider.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DeviceUser(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val branch: String,
    val enrollment: String,
    val imei: String,
    val dataConfig: String,
    val configuratoorEnrollment: String,
    val dateUpdate: String?,
    val accepted: Boolean = false,
    var name: String? = null

)