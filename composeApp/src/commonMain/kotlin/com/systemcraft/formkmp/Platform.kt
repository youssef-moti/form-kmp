package com.systemcraft.formkmp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform