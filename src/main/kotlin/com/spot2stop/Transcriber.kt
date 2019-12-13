package com.spot2stop

import com.google.auth.oauth2.ServiceAccountCredentials
import com.spot2stop.audioconversion.FFMpegAudioConverter
import com.spot2stop.storage.StorageServiceImpl
import com.spot2stop.transcription.TranscriberImpl
import java.io.File
import java.io.FileInputStream


fun main(args: Array<String>) {
    println("parsing options")
    val configuration = TranscriptionConfiguration.parse(args)

    println("Reading credentials")
    val credentialsStream = FileInputStream(File(configuration.credentialsPath))
    val serviceAccountCredentials = ServiceAccountCredentials.fromStream(credentialsStream)

    println("Converting file")
    val convertedFile = FFMpegAudioConverter(configuration).convert()

    println("storing blob")
    val blobPath = StorageServiceImpl(configuration, serviceAccountCredentials).store(convertedFile)

    println("Performing conversion")
    TranscriberImpl(serviceAccountCredentials, blobPath, configuration.sourceFileName, configuration.sourceFileLanguage).performConversion()

}

