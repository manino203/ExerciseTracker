package com.example.exercisetracker.backend.data.db

import com.example.exercisetracker.backend.data.db.entities.BodyPart
import com.example.exercisetracker.backend.data.db.entities.Exercise
import com.example.exercisetracker.backend.data.db.entities.ExerciseDetails
import kotlinx.coroutines.flow.Flow

class ExerciseDataRepository(
    private val db: ExerciseDatabase
) {

    fun getBodyParts(): Flow<List<BodyPart>>{
        return db.dao.getBodyParts()
    }

    suspend fun getBodyPart(id: Int): BodyPart{
        return db.dao.getBodyPart(id)
    }

    suspend fun updateBodyPart(bodyPart: BodyPart){
        db.dao.upsertBodyPart(bodyPart)
    }
    suspend fun updateBodyParts(bodyParts: List<BodyPart>){
        bodyParts.forEach {
            updateBodyPart(it)
        }
    }

    fun getExercises(bodyPartId: Int): Flow<List<Exercise>> {
        return db.dao.getExercises(bodyPartId)
    }
    suspend fun getExercise(id: Int): Exercise{
        return db.dao.getExercise(id)
    }
    suspend fun getLatestDetailsId(exerciseId: Int): Int{
        return db.dao.getLatestDetails(exerciseId).exerciseId
    }

    suspend fun getLatestDetails(exerciseId: Int): ExerciseDetails{
        return db.dao.getLatestDetails(exerciseId)
    }

    suspend fun updateExercise(exercise: Exercise){
        db.dao.upsertExercise(exercise)
    }

    suspend fun deleteExercise(exercise: Exercise){
        db.dao.deleteExercise(exercise)
    }


    fun getExerciseDetails(exerciseId: Int): Flow<List<ExerciseDetails>> {
        return db.dao.getDetails(exerciseId)
    }

    suspend fun updateExerciseDetails(details: ExerciseDetails){
        db.dao.upsertDetails(details)
    }

    suspend fun deleteDetails(exerciseDetails: ExerciseDetails){
        db.dao.deleteDetails(exerciseDetails)
    }


}