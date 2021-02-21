package com.pupptmstr.taskmanager.server

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.pupptmstr.taskmanager.models.EternalErrorResponse
import com.pupptmstr.taskmanager.models.Path
import com.pupptmstr.taskmanager.models.Task
import com.pupptmstr.taskmanager.models.TaskDeserializer
import com.sun.net.httpserver.HttpExchange
import java.io.*
import java.lang.NumberFormatException
import java.time.LocalDate

class ClientHandler(private val exchange: HttpExchange, private val taskManager: TaskManager) : Runnable {

    override fun run() {
        println("handled exchange")
        val path = Path(exchange.requestURI.path)
        val method = exchange.requestMethod
        val inputStream: InputStream = exchange.requestBody
        val stringBuilder = StringBuilder()
        BufferedReader(InputStreamReader(inputStream)).lines().forEach {
            stringBuilder.append(it).append("\n")
        }
        val requestBody = stringBuilder.toString()

            when (method) {
            "GET" -> {
                if (path.base == "get") {
                    when (path.tale) {
                        "", "all" -> {
                            handleOkResponse(taskManager.all)
                        }
                        else -> {
                            try {
                                val id = path.tale.toInt()
                                val response = taskManager.getById(id)
                                if (response != "error") {
                                    handleOkResponse(taskManager.getById(id))
                                } else {
                                    handleNotFound()
                                }
                            } catch (e: NumberFormatException) {
                                handleNotFound()
                            }
                        }
                    }
                } else {
                    handleNotFound()
                }
            }
            "POST" -> {
                when (path.base) {
                    "update", "change" -> {
                        val response = taskManager.changeById(getTaskFromRequest(requestBody))
                        if (response != "error") {
                            handleOkResponse(response)
                        } else {
                            handleNotFound()
                        }
                    }
                    "create" -> {
                        val response = taskManager.create(getTaskFromRequest(requestBody))
                        if (response != "error") {
                            handleOkResponse(response)
                        } else {
                            val builder = GsonBuilder()
                            val gson: Gson = builder.create()
                            handleEternalError(gson.toJson(EternalErrorResponse("Can't get task from request")))
                        }
                    }
                    "delete", "remove" -> {
                        val response = taskManager.deleteById(getIdFromRequest(requestBody))
                        if (response != "error") {
                            handleOkResponse(response)
                        } else {
                            handleBadRequest()
                        }
                    }
                    else -> {
                        handleNotFound()
                    }
                }
            }
        }
    }

    private fun getIdFromRequest(body: String): Int {
        return try {
            val task = getTaskFromRequest(body)
            task.id
        } catch (e: Exception) {
            -1
        }
    }

    private fun getTaskFromRequest(body: String): Task {
        return try {
            val builder = GsonBuilder()
            val gson: Gson = builder.registerTypeAdapter(Task::class.java, TaskDeserializer()).create()
            gson.fromJson(body, Task::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            Task(-1, "error", LocalDate.now(), LocalDate.now(), "error")
        }
    }

    private fun handleNotFound() {
        handleResponse("Not Found", 404)
    }

    private fun handleOkResponse(body: String) {
        handleResponse(body, 200)
    }

    private fun handleBadRequest() {
        handleResponse("Bad request", 400)
    }

    private fun handleEternalError(body: String) {
        handleResponse(body, 500)
    }

    private fun handleResponse(
        body: String,
        code: Int
    ) {
        val outputStream = BufferedOutputStream(exchange.responseBody)
        val resBody = body.toByteArray()
        exchange.responseHeaders.set("Content-Type", "application/json")
        exchange.sendResponseHeaders(code, resBody.size.toLong())
        outputStream.write(resBody)
        outputStream.flush()
        outputStream.close()
    }
}