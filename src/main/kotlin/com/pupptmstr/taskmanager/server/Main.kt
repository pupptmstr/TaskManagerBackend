package com.pupptmstr.taskmanager

import com.pupptmstr.taskmanager.server.Server
import com.sun.net.httpserver.HttpServer
import java.net.InetSocketAddress

fun main() {
    val server = HttpServer.create(InetSocketAddress(8081), 0)
    server.createContext("/", Server())
    server.start()
    println("server started")
}
