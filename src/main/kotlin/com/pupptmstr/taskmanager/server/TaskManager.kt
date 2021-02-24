package com.pupptmstr.taskmanager.server

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.pupptmstr.taskmanager.models.Task


class TaskManager {
    private var body = mutableMapOf<Int, Task>()
    private val builder = GsonBuilder()
    private val gson: Gson = builder.setPrettyPrinting().create()

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
            body.remove(id)
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
            val keys = body.keys
            var newId = 0
            for (i in keys) {
                if (i == newId) {
                    newId++
                }
            }
            val newTask = Task(newId, task.status, task.startTime, task.deadline, task.description)
            body[newId] = newTask
            gson.toJson(body[newId])
        } catch (e: Exception) {
            "error"
        }
    }
}