package com.example.irohcomposemultiplatform

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform