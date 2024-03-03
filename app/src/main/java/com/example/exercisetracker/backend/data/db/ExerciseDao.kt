package com.example.exercisetracker.backend.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.exercisetracker.backend.data.db.entities.BodyPart
import com.example.exercisetracker.backend.data.db.entities.Exercise
import com.example.exercisetracker.backend.data.db.entities.ExerciseDetails
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseDao {

    @Upsert
    suspend fun upsertExercise(exercise: Exercise)

    @Upsert
    suspend fun upsertDetails(details: ExerciseDetails)

    @Upsert
    suspend fun upsertBodyPart(bodyPart: BodyPart)

    @Delete
    suspend fun deleteExercise(exercise: Exercise)

    @Delete
    suspend fun deleteDetails(details: ExerciseDetails)

    @Delete
    suspend fun deleteBodyPart(bodyPart: BodyPart)

    @Transaction
    @Query("SELECT * FROM Exercise WHERE bodyPartId = :bodyPartId ORDER BY position ASC")
    fun getExercises(bodyPartId: Int): Flow<List<Exercise>>

    @Transaction
    @Query("SELECT * FROM Exercise WHERE id = :id LIMIT 1")
    suspend fun getExercise(id: Int): Exercise

    @Transaction
    @Query("SELECT * FROM ExerciseDetails WHERE exerciseId = :exerciseId ORDER BY timestamp DESC")
    fun getDetails(exerciseId: Int): Flow<List<ExerciseDetails>>
    @Transaction
    @Query("SELECT * FROM BodyPart ORDER BY position ASC")
    fun getBodyParts(): Flow<List<BodyPart>>

    @Transaction
    @Query("SELECT * FROM ExerciseDetails WHERE exerciseId = :exerciseId ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLatestDetails(exerciseId: Int): ExerciseDetails

    @Transaction
    @Query("SELECT * FROM BodyPart WHERE id = :id")
    suspend fun getBodyPart(id: Int): BodyPart

}