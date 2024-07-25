package com.example.io_example.controller

import android.util.Log
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

open class Command {
    companion object {
        private const val TAG = "Command"
    }

    private val root: String = "su"
    private val commandArgs: String = "-c"
    protected fun executeRootCommand(command: String, args: String = ""): Boolean {
        val execCommand = "$root $commandArgs $command $args"
        try {
            Log.d(TAG, "Executing root command: $execCommand")
            val process = Runtime.getRuntime().exec(execCommand.split(" ").toTypedArray())
            val result = process.waitFor()
            if (result != 0) {
                logError(process, execCommand)
                throw IOException("Failed to execute command: $execCommand")
            }
            process.destroy()
            Log.d(TAG, "Executed root command: $execCommand")
            return true
        } catch (e: Exception) {
            Log.e(TAG, "Failed to execute root command: $execCommand", e)
            return false
        }
    }

    protected fun readRootCommand(command: String, args: String = ""): String {
        val execCommand = "$root $commandArgs $command $args"
        return try {
            Log.d(TAG, "Executing root command: $execCommand")
            val process = Runtime.getRuntime().exec(execCommand.split(" ").toTypedArray())
            val reader = BufferedReader(InputStreamReader(process.inputStream))
            val result = StringBuilder()
            var line: String?

            while (reader.readLine().also { line = it } != null) {
                result.append(line)
            }

            reader.close()
            process.waitFor()

            if (process.exitValue() != 0) {
                logError(process, execCommand)
                throw IOException("Failed to read root command: $execCommand")
            }
            process.destroy()

            Log.d(TAG, "Executed root command: $execCommand")
            result.toString()
        } catch (e: Exception) {
            Log.e(TAG, "Failed to read root command: $execCommand", e)
            ""
        }
    }

    private fun logError(process: Process, command: String) {
        val errorReader = BufferedReader(InputStreamReader(process.errorStream))
        val errorMessage = StringBuilder()
        var errorLine: String?

        while (errorReader.readLine().also { errorLine = it } != null) {
            errorMessage.append(errorLine)
        }

        errorReader.close()
        Log.e(TAG, "Error executing command: $command\n$errorMessage")
    }
}