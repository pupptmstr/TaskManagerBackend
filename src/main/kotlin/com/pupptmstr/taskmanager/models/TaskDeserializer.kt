package com.pupptmstr.taskmanager.models

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import java.lang.reflect.Type
import java.time.LocalDate

class TaskDeserializer : JsonDeserializer<Task> {
    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Task {
        val jsonObject = json!!.asJsonObject
        val id: Int = jsonObject.get("id").asInt
        val status: String = jsonObject.get("status").asString
        val startTime: LocalDate
        val deadline: LocalDate
        val description: String = jsonObject.get("description").asString

        var timeSplit = jsonObject.get("start-time").asString.split("-")
        var year = timeSplit[0].toInt()
        var month = timeSplit[1].toInt()
        var day = timeSplit[2].toInt()
        var localDate = LocalDate.of(year, month, day)
        startTime = localDate

        timeSplit = jsonObject.get("deadline").asString.split("-")
        year = timeSplit[0].toInt()
        month = timeSplit[1].toInt()
        day = timeSplit[2].toInt()
        localDate = LocalDate.of(year, month, day)
        deadline = localDate

        return Task(id, status, startTime, deadline, description)
    }
}