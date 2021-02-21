package com.pupptmstr.taskmanager.models

import com.google.gson.annotations.SerializedName
import java.time.LocalDate

data class Task(
    @SerializedName("id")
    val id: Int,

    @SerializedName("status")
    val status: String,

    @SerializedName("start-time")
    val startTime: LocalDate,

    @SerializedName("deadline")
    val deadline: LocalDate,

    @SerializedName("description")
    val description: String


)