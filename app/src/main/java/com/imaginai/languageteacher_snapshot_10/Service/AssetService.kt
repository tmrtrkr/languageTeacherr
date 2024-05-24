package com.imaginai.languageteacher_snapshot_10.Service
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import kotlinx.coroutines.runBlocking
import okio.buffer
import okio.source
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
class AssetService {

     fun readAssetFileToTempFile(context: Context, assetPath: String): File {
        val assetManager = context.assets
        val tempFile = File(context.cacheDir, "temp_audio.wav")

        assetManager.open(assetPath).use { inputStream ->
            FileOutputStream(tempFile).use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }
        return tempFile
    }
}