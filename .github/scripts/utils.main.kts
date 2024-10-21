#!/usr/bin/env kotlin

import java.io.FileInputStream
import java.util.Properties



fun readProperties(filePath: String): Properties {
    val properties = Properties()
    properties.load(FileInputStream(filePath))
    return properties
}