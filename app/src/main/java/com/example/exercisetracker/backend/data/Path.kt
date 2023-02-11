package com.example.exercisetracker.backend.data

@Suppress("MemberVisibilityCanBePrivate")
class Path(
    val bodyPart: String,
    val exerciseId: String? = null
) {
    fun get(): String {
        return if (exerciseId != null) {
            "$bodyPart/$exerciseId"
        } else {
            bodyPart
        }
    }
}