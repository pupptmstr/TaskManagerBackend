package com.pupptmstr.taskmanager.server

import com.sun.net.httpserver.HttpServer
import java.net.InetSocketAddress

fun main() {
    val server = HttpServer.create(InetSocketAddress(8081), 0)
    server.createContext("/", Server())
    server.start()
    println("server started")
    var isWorking = true
    while (isWorking) {
        val command = readLine()
        if (command?.toLowerCase() == "stop" || command?.toLowerCase() == "quit") {
            server.stop(0)
            println("server is stopping")
            isWorking = false
        }
    }
    println("server stopped")
}
