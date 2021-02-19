package com.pupptmstr.taskmanager

class Path(path: String) {
    val base: String
    val tale: String

    init {
        val pathSplit = path.split("/")
        base = pathSplit[1].toLowerCase()
        tale = if (pathSplit.size == 1) "" else path.toLowerCase().replace(base, "").replace("/", "")
    }
}