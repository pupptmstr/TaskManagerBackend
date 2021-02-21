package com.pupptmstr.taskmanager.models

import com.google.gson.annotations.SerializedName

data class EternalErrorResponse(
    @SerializedName("error")
    val error: String
)