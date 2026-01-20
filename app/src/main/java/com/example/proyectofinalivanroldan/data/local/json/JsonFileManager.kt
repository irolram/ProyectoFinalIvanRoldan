package com.example.proyectofinalivanroldan.data.local.json

import android.content.Context

class JsonFileManager(private val context: Context) {

    fun read(fileName: String): String {
        return context.openFileInput(fileName)
            .bufferedReader()
            .use { it.readText() }
    }

    fun write(fileName: String, content: String) {
        context.openFileOutput(fileName, Context.MODE_PRIVATE)
            .use { it.write(content.toByteArray()) }
    }
}
