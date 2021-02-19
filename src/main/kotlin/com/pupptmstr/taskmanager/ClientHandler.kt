package com.pupptmstr.taskmanager

import com.sun.net.httpserver.HttpExchange
import java.io.InputStream
import java.lang.NumberFormatException

class ClientHandler(private val exchange: HttpExchange, private val taskManager: TaskManager) : Runnable {

    override fun run() {
        println("handled exchange")
        val path = Path(exchange.requestURI.path)
        val method = exchange.requestMethod
        val requestBody = exchange.requestBody.readAllBytes()

        when (method) {
            "GET" -> {
                if (path.base == "get") {
                    when (path.tale) {
                        "", "all" -> {
                            handleOkResponse(taskManager.getAll())
                        }
                        else -> {
                            try {
                                val id = path.tale.toInt()
                                handleOkResponse(taskManager.getById(id))
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
                        handleOkResponse(taskManager.changeById(getIdFromRequest(requestBody)))
                    }
                    "create" -> {
                        handleOkResponse(taskManager.create(getTaskFromRequest(requestBody)))
                    }
                    "delete", "remove" -> {
                        handleOkResponse(taskManager.deleteById(getIdFromRequest(requestBody)))
                    }
                    else -> {
                        handleNotFound()
                    }
                }

            }
        }


    }

    private fun getIdFromRequest(body: ByteArray) : Int {
        return 1
    }

    private fun getTaskFromRequest(body: ByteArray) : Task {
        return Task()
    }

    private fun handleNotFound() {
        handleResponse("Not Found", 404)
    }

    private fun handleOkResponse(body: String) {
        handleResponse(body, 200)
    }

    private fun handleResponse(
        body: String,
        code: Int
    ) {
        val outputStream = exchange.responseBody
        exchange.responseHeaders.set("Content-Type", "application/json")
        exchange.sendResponseHeaders(code, body.length.toLong())
        outputStream.write(body.toByteArray())
        outputStream.flush()
        outputStream.close()
    }
}