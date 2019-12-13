package com.spot2stop.audioconversion

import java.nio.file.Path

interface AudioConverter {
    fun convert(): Path
}