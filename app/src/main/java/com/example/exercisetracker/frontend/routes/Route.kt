package com.example.exercisetracker.frontend.routes

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class Route(val destinationName: String, val args: List<NamedNavArgument> = listOf()) {

    val route: String = destinationName.plus(args.joinToString { "/{${it.name}}" })

    fun createRoute(vararg args: String) = "$destinationName${args.joinToString{ "/$it" }}"

    fun createPath(vararg args: String) = if (args.isEmpty()) destinationName else args.joinToString("/").apply {
        slice(1 until length)
    }



    object BodyParts: Route("BodyParts")
    object Exercises: Route("Exercises", listOf(
        navArgument("bodyPart"){
            type = NavType.StringType
        }))
    object ExerciseData: Route(
        "ExerciseData",
        listOf(
            navArgument("bodyPart"){
                type = NavType.StringType
            },
            navArgument("exercise"){
                type = NavType.StringType
            }
    ))
}