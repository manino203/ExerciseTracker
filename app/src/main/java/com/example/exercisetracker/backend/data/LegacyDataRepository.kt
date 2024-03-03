package com.example.exercisetracker.backend.data

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.exercisetracker.BuildConfig
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.first
import java.io.File
import java.io.FileWriter
import java.io.IOException


class LegacyDataRepository(
    val context: Context,
    val gson: Gson
) {

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


    suspend fun getDataUri(): Uri? {
        var uri: Uri? = null
        val filePath = "data.txt"
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
            uri =
                FileProvider.getUriForFile(context, "${BuildConfig.APPLICATION_ID}.provider", file)
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

    suspend fun importData(uri: Uri): Boolean {
        var success = false
        val inputStream = context.contentResolver.openInputStream(uri)
        context.dataStore.edit { prefs ->

            inputStream?.let { stream ->
                stream.reader().readLines().map { line ->
                    line.split(Regex(" = "), 2)
                }.takeIf {
                    validateImportData(it)
                }?.let { data ->
                    prefs.clear()
                    data.forEach {
                        prefs[stringPreferencesKey(it[0])] = it[1]
                    }
                    success = true
                }
            }
        }
        inputStream?.close()
        return success
    }

    private fun validateImportData(lines: List<List<String>>): Boolean {

        fun <T> isConvertableToObject(value: String): Boolean {
            return try {
                val type = (object : TypeToken<List<T>>() {}).type
                gson.fromJson<List<T>>(value, type)
                true
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }


        lines.forEach { pair ->
            try {
                if (
                    !(isConvertableToObject<BodyPart>(pair[1]) ||
                            isConvertableToObject<Exercise>(pair[1]) ||
                            isConvertableToObject<ExerciseDetails>(pair[1]))
                ) {
                    return false
                }
            } catch (e: IndexOutOfBoundsException) {
                return false
            }
        }

        return true
    }
}




