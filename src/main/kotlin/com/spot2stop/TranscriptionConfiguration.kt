package com.spot2stop

data class TranscriptionConfiguration(
        val sourceFileName : String,
        val sourceFileLanguage: String,
        val bucketName : String,
        val ffmpegPath : String,
        val credentialsPath : String
) {

    companion object Parser {
        fun parse(args: Array<String>) : TranscriptionConfiguration {
            var i = 0

            var sourceFileName : String? = null
            var sourceFileLanguage : String? = null
            var bucketName : String? = null
            var ffmpegPath : String? = null
            var credentialsPath: String? = null

            while (i<args.size) {
                val command = args[i++].substring(2).toUpperCase()
                val value = args[i++]
                when(command) {
                    "SOURCE"    -> sourceFileName = value
                    "LANGUAGE"  -> sourceFileLanguage = value
                    "BUCKET"    -> bucketName = value
                    "FFMPEGPATH"-> ffmpegPath = value
                    "CREDS"     -> credentialsPath = value
                    else        -> println("Unrecognised $command")
                }
            }

            return TranscriptionConfiguration(  sourceFileName!!,
                                                sourceFileLanguage!!,
                                                bucketName!!,
                                                ffmpegPath!!,
                                                credentialsPath!!
            )
        }
    }

}

