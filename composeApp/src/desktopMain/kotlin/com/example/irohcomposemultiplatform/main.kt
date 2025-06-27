package com.example.irohcomposemultiplatform

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Iroh Compose Multiplatform",
    ) {
        App()
    }
}