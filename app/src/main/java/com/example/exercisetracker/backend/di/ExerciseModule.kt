package com.example.exercisetracker.backend.di

import android.content.Context
import androidx.room.Room
import com.example.exercisetracker.R
import com.example.exercisetracker.backend.data.LegacyDataRepository
import com.example.exercisetracker.backend.data.db.ExerciseDataRepository
import com.example.exercisetracker.backend.data.db.ExerciseDatabase
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
    fun provideLegacyDataRepository(
        @ApplicationContext app: Context,
        gson: Gson
    ): LegacyDataRepository {
        return LegacyDataRepository(app, gson)
    }

    @Provides
    fun provideIODispatcher() = Dispatchers.IO

    @Provides
    fun provideTitle(@ApplicationContext context: Context) =
        context.resources.getString(R.string.app_name)


    @Singleton
    @Provides
    fun provideExerciseDataRepository(@ApplicationContext context: Context) =
        ExerciseDataRepository(
            Room.databaseBuilder(
                context,
                ExerciseDatabase::class.java,
                "exercise_tracker.db"
            ).build()
        )
}


