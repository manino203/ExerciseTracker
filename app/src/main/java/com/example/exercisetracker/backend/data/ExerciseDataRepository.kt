package com.example.exercisetracker.backend.data

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import androidx.core.content.FileProvider
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.exercisetracker.BuildConfig
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import java.io.File
import java.io.FileWriter
import java.io.IOException


class ExerciseDataRepository(
    val context: Context,
    val gson: Gson
) {

    val scope = CoroutineScope(Dispatchers.IO)
    companion object {
        const val FILE_NAME = "exercises"
        val Context.dataStore: DataStore<androidx.datastore.preferences.core.Preferences> by preferencesDataStore(
            FILE_NAME
        )

    }

    suspend fun readValue(key: String, defaultValue: String = ""): String {


        var result = defaultValue
        context.dataStore.data.first().let {

            it[stringPreferencesKey(key)]?.let { savedValue ->

                result = savedValue
            }
        }
        return result
    }

    private suspend fun saveValue(key: String, newValue: String) {

        context.dataStore.edit {
            it[stringPreferencesKey(key)] = newValue
        }
    }

    suspend fun deleteValue(_key: String) {
        val key = stringPreferencesKey(_key)
        context.dataStore.edit {
            if (it.contains(key)) {
                it.remove(key)
            }
        }
    }

    suspend inline fun <reified itemType> readList(key: String): List<itemType> {
        return try {
            val type = (object : TypeToken<List<itemType>>() {}).type
            gson.fromJson(readValue(key), type)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun saveList(key: String, newList: List<Any>) {

        saveValue(key, Gson().toJson(newList))
    }


    suspend fun getDataUri(): Uri?{
        var uri: Uri? = null
        val filePath = "data"
        val file = File(context.filesDir, filePath)
        if (file.exists()) {
            file.delete()
        }
        file.createNewFile()

        val data = context.dataStore.data.first()

        val writer = FileWriter(file)

        for ((key, value) in data.asMap()) {
            writer.append("$key = $value\n")
        }

        writer.close()

        try {
            uri = FileProvider.getUriForFile(context, "${BuildConfig.APPLICATION_ID}.provider", file)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return uri
    }

    fun importData(){
        val initialUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Downloads.EXTERNAL_CONTENT_URI
        } else {
            Uri.fromFile(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS))
        }
        context.startActivity(Intent(Intent.ACTION_OPEN_DOCUMENT).apply{
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "*/*"
            putExtra(DocumentsContract.EXTRA_INITIAL_URI, initialUri)
        })
    }

}




