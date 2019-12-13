package com.spot2stop.audioconversion

import com.github.kokorin.jaffree.StreamType
import com.github.kokorin.jaffree.ffmpeg.FFmpeg
import com.github.kokorin.jaffree.ffmpeg.UrlInput
import com.github.kokorin.jaffree.ffmpeg.UrlOutput
import com.spot2stop.TranscriptionConfiguration
import java.io.File
import java.nio.file.Path
import java.nio.file.Paths
import java.util.concurrent.TimeUnit

class FFMpegAudioConverter(private val configuration: TranscriptionConfiguration) : AudioConverter {
    override fun convert(): Path {

        val property = System.getProperty("java.io.tmpdir")
        val s = "" + System.currentTimeMillis() + ".flac"
        val createTempFile = File(property, s)
        val path = Paths.get(createTempFile.path)!!

        val addOutput = FFmpeg.atPath(Paths.get(configuration.ffmpegPath)).addInput(UrlInput.fromPath(Paths.get(configuration.sourceFileName)))
                .addArguments("-ar", "16000")
                .addArguments("-ac", "1")
                .addArgument("-vn")
                .addOutput(UrlOutput.toPath(path).setCodec(StreamType.AUDIO, "flac"))
                .setProgressListener { progress -> println(progress.getTime(TimeUnit.SECONDS)) }

        addOutput.execute()

        return path

    }


}