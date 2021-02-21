package com.pupptmstr.taskmanager.server

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.pupptmstr.taskmanager.models.Task


class TaskManager {
    private var body = mutableListOf<Task>()
    private val builder = GsonBuilder()
    private val gson: Gson = builder.create()

    @get:Synchronized
    val all: String
        get() = gson.toJson(body)

    @Synchronized
    fun getById(id: Int): String {
        return try {
            gson.toJson(body[id])
        } catch (e: IndexOutOfBoundsException) {
            "error"
        }
    }

    @Synchronized
    fun changeById(task: Task): String {
        return try {
            val id = task.id
            body[id] = task
            gson.toJson(task)
        } catch (e: IndexOutOfBoundsException) {
            "error"
        }
    }

    @Synchronized
    fun deleteById(id: Int): String {
        return try {
            val element = body[id]
            body.removeAt(id)
            gson.toJson(element)
        } catch (e: IndexOutOfBoundsException) {
            "error"
        }
    }

    @Synchronized
    fun create(task: Task): String {
        return try {
            if (task.id < 0) {
                throw IndexOutOfBoundsException()
            }
            body.add(task)
            gson.toJson(body[body.lastIndex])
        } catch (e: Exception) {
            "error"
        }
    }
}