package com.example.exercisetracker.backend.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.exercisetracker.backend.data.db.entities.BodyPart
import com.example.exercisetracker.backend.data.db.entities.Exercise
import com.example.exercisetracker.backend.data.db.entities.ExerciseDetails


@Database(
    entities = [BodyPart::class, Exercise::class, ExerciseDetails::class] ,
    version = 1
)
abstract class ExerciseDatabase: RoomDatabase() {
    abstract val dao: ExerciseDao

}