package com.spot2stop.transcription

import com.google.api.gax.core.FixedCredentialsProvider
import com.google.auth.Credentials
import com.google.cloud.speech.v1.*
import com.google.common.io.Files
import java.io.File

class TranscriberImpl(private val credentials : Credentials,
                      private val sourceGcloudUrl : String,
                      private val sourceFileName : String,
                      private val sourceLanguage : String = "fr-FR") : TranscriptionService {

    override fun performConversion() {
        val speechClient = SpeechClient.create(SpeechSettings.newBuilder().setCredentialsProvider(FixedCredentialsProvider.create(credentials)).build())
        val config = RecognitionConfig.newBuilder()
                .setEncoding(RecognitionConfig.AudioEncoding.FLAC)
                .setLanguageCode(sourceLanguage)
                .setSampleRateHertz(16000)
                .build()

        val audio = RecognitionAudio.newBuilder().setUri(sourceGcloudUrl).build()

        val response = speechClient.longRunningRecognizeAsync(config, audio)

        var i = 0
        while (!response.isDone) {
            println("Waiting $i")
            Thread.sleep(10000)
            i++
        }


        val resultFileName = Files.getNameWithoutExtension(sourceFileName).plus(".txt")
        println("Results will be stored in $resultFileName")
        File(resultFileName).printWriter().use { out ->
            for (result in response.get().resultsList) {
                // There can be several alternative transcripts for a given chunk of speech. Just use the
                // first (most likely) one here.
                val alternativesList = result.alternativesList
                for (speechRecognitionAlternative in alternativesList) {
                    val speechRecognitionAlternative1: SpeechRecognitionAlternative = speechRecognitionAlternative
                    out.println("Transcription: ${speechRecognitionAlternative1.transcript}")
                }
            }
        }

    }


}