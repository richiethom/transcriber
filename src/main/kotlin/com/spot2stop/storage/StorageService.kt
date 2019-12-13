package com.spot2stop.storage

import java.nio.file.Path

interface StorageService {
    fun store(convertedFile: Path): String
}