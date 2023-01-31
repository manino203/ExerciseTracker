package com.example.exercisetracker.backend.data

class Path(
    val bodyPart: String,
    val exerciseId: String? = null
) {
    fun get(): String {
        return if (exerciseId != null){
            "$bodyPart/$exerciseId"
        }else{
            bodyPart
        }
    }
}