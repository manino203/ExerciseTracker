package com.example.exercisetracker.backend.data

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.first


class ExerciseDataRepository(
    private val context: Context,
    val gson: Gson
    ) {

    companion object {
        private const val FILE_NAME =  "exercises"
        private val Context.dataStore: DataStore<androidx.datastore.preferences.core.Preferences> by preferencesDataStore(
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
        Log.d("dataStoreGet: $key", "got: $result")
        return result
    }

    private suspend fun saveValue(key: String, newValue: String) {
        var old = ""
        context.dataStore.edit {
            old = it[stringPreferencesKey(key)].toString()
            it[stringPreferencesKey(key)] = newValue
        }
        Log.d("dataStoreEdit: $key", "from: $old to: $newValue")
    }

//    private inline fun <reified T : List<Any>> Gson.fromJson(json: String): T {
//        return fromJson<T>(json, object : TypeToken<T>() {}.type)
//    }

    suspend inline fun <reified itemType> readList(key: String): List<itemType> {

        return try {
            val type = (object: TypeToken<List<itemType>>() {}).type
            gson.fromJson(readValue(key), type)
        } catch (e: NullPointerException) {
            emptyList()
        }
    }

    suspend fun saveList(key: String, newList: List<Any>) {

        saveValue(key, Gson().toJson(newList))
    }



}