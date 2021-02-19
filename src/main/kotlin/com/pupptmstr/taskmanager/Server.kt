package com.pupptmstr.taskmanager

import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpHandler
import java.util.concurrent.Executors

class Server : HttpHandler {
    private val taskManager = TaskManager()
    private val executor = Executors.newFixedThreadPool(2)

    override fun handle(exchange: HttpExchange?) {
        executor.execute(ClientHandler(exchange!!, taskManager))
    }
}