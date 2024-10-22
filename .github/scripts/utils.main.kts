#!/usr/bin/env kotlin

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.Properties

fun readProperties(filePath: String): Properties {
    val properties = Properties()
    properties.load(FileInputStream(filePath))
    return properties
}

fun writeUpdatedVersion(filePath: String, properties: Properties, key: String, value: String) {
    properties.setProperty(key, value)
    properties.store(FileOutputStream(filePath), "$value version update")
}

fun executeCommandOnShell(command: String): Int {
    val process = ProcessBuilder("/bin/bash", "-c", command).inheritIO().start()
    return process.waitFor()
}

fun replaceTextInFile(filePath: String, currentText: String, updatedText: String) {
    val file = File(filePath)
    if (!file.exists()) {
        throw IllegalStateException("File does not exist")
    }
    val fileContent = file.readText()
    val updatedFileContent = fileContent.replaceFirst(currentText, updatedText)
    file.writeText(updatedFileContent)
}