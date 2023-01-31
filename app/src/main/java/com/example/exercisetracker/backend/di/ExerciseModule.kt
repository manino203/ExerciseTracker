package com.example.exercisetracker.backend.di

import android.content.Context
import com.example.exercisetracker.backend.data.ExerciseDataRepository
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object ExerciseModule {

    @Singleton
    @Provides
    fun provideGson() = Gson()

    @Singleton
    @Provides
    fun provideExerciseDataRepository(
        @ApplicationContext app: Context,
        gson: Gson
    ): ExerciseDataRepository {
        return ExerciseDataRepository(app, gson)
    }

    @Provides
    fun provideIODispatcher() = Dispatchers.IO

}


