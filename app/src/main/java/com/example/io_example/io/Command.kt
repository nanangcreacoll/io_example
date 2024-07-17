package com.example.io_example.io

import android.content.ContentValues
import android.util.Log
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

open class Command {
    protected fun executeRootCommand(command: String, args: String) {
        try {
            val process = Runtime.getRuntime().exec(arrayOf("su", "-c", command, args))
            val result = process.waitFor()
            if (result != 0) {
                throw IOException("Failed to execute command: $command $args")
            }
            Log.d(ContentValues.TAG, "Executed root command: $command $args")
        } catch (e: Exception) {
            Log.e(ContentValues.TAG, "Failed to execute root command: $command $args", e)
        }
    }

    protected fun readRootCommand(command: String, args: String): String {
        return try {
            val process = Runtime.getRuntime().exec(arrayOf("su", "-c", command, args))
            val reader = BufferedReader(InputStreamReader(process.inputStream))
            val result = StringBuilder()
            var line: String?

            while (reader.readLine().also { line = it } != null) {
                result.append(line)
            }

            reader.close()
            process.waitFor()

            if (process.exitValue() != 0) {
                throw IOException("Failed to read command: $command $args")
            }

            result.toString()
        } catch (e: Exception) {
            Log.e(ContentValues.TAG, "Failed to read command: $command $args", e)
            ""
        }
    }
}