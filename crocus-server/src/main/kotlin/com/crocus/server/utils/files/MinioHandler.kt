package com.crocus.server.utils.files

import com.crocus.server.entity.entities.MinioFile
import io.minio.BucketExistsArgs
import io.minio.GetObjectArgs
import io.minio.GetPresignedObjectUrlArgs
import io.minio.MakeBucketArgs
import io.minio.MinioClient
import io.minio.PutObjectArgs
import io.minio.RemoveObjectArgs
import io.minio.http.Method
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.io.InputStream


@Component
class MinioHandler(
    private val minioClient: MinioClient, @Value("\${minio.bucket-name}") private val bucketName: String
) {

    private val logger = KotlinLogging.logger(this::class.java.name)

    init {
        // 初始化时判断的兜底方法
        createBucketIfNotExists()
    }

    private fun createBucketIfNotExists() {
        val checkArgs = BucketExistsArgs.builder().bucket(bucketName).build()
        val exists = minioClient.bucketExists(checkArgs)
        if (!exists) {
            val makeArgs = MakeBucketArgs.builder().bucket(bucketName).build()
            minioClient.makeBucket(makeArgs)
        }
    }

    suspend fun uploadObject(file: MinioFile) {
        val size = file.size ?: throw Exception("File size is required")
        when {
            file.`object`.isNullOrBlank() -> throw Exception("object is required")
            file.content == null -> throw Exception("InputStream is required")
            size <= 0 -> throw Exception("File size is required")
            file.type.isNullOrBlank() -> throw Exception("File type is required")
        }

        val putArgs = PutObjectArgs.builder()
            .bucket(bucketName)
            .`object`(file.`object`)
            .stream(file.content, size, -1)
            .contentType(file.type)
            .build()
        val result = minioClient.putObject(putArgs)

        logger.info { "上传文件: ${file.fileName} to $bucketName, ${file.`object`} Size: ${size} ContentType: ${file.type} || ${result.etag()}" }
    }

    suspend fun downloadObject(objectName: String): InputStream {
        val getArgs = GetObjectArgs.builder()
            .bucket(bucketName)
            .`object`(objectName)
            .build()
        return minioClient.getObject(getArgs)
    }

    suspend fun deleteObject(objectName: String) {
        val removeArgs = RemoveObjectArgs.builder()
            .bucket(bucketName)
            .`object`(objectName)
            .build()
        minioClient.removeObject(removeArgs)
        logger.info { "删除文件: $objectName in $bucketName" }
    }

    suspend fun getUrl(objectName: String, expiry: Long = 30 * 60): String {
        val urlArgs = GetPresignedObjectUrlArgs.builder()
            .method(Method.GET)
            .bucket(bucketName)
            .`object`(objectName)
            .expiry(expiry.coerceAtMost(604800L).toInt())
            .build()
        return minioClient.getPresignedObjectUrl(urlArgs)
    }

}