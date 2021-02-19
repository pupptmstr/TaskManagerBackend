package com.pupptmstr.taskmanager

class TaskManager {
    val body = ArrayList<Task>()

    fun getAll() : String {
        return "all"
    }

    fun getById(id: Int) : String {
        return "get $id"
    }

    fun changeById(id: Int) : String {
        return "changed $id"
    }

    fun deleteById(id: Int) : String {
        return "deleted $id"
    }

    fun create(task: Task) : String {
        return "created"
    }
}