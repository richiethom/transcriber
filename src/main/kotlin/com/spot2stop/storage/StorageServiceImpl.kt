package com.spot2stop.storage

import com.google.auth.oauth2.ServiceAccountCredentials
import com.google.cloud.storage.Acl
import com.google.cloud.storage.BlobInfo
import com.google.cloud.storage.StorageOptions
import com.spot2stop.TranscriptionConfiguration
import java.io.FileInputStream
import java.nio.file.Path
import java.util.*

class StorageServiceImpl(private val configuration: TranscriptionConfiguration,
                         private val serviceAccountCredentials: ServiceAccountCredentials) : StorageService {
    override fun store(convertedFile: Path): String {
        println("Storing $convertedFile on Google")
        val fileInputStream = FileInputStream(convertedFile.toFile())

        //2. store the file on GCloud
        val storage = StorageOptions.newBuilder().setCredentials(serviceAccountCredentials).build().service
        val acls = ArrayList<Acl>()

        val name = convertedFile.fileName.toFile().name
        val blob =
                storage.create(
                        BlobInfo.newBuilder(configuration.bucketName, name).setAcl(acls).build(),
                        fileInputStream)


        println(blob.blobId)
        println(blob.selfLink)


        //"gs://rapid-access-227421/1546550644205.flac"
        return "gs://${configuration.bucketName}/$name"
    }
}