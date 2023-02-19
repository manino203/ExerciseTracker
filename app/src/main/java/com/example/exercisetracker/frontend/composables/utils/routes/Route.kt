package com.example.exercisetracker.frontend.composables.utils.routes

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class Route(val destinationName: String, val args: List<NamedNavArgument> = listOf()) {

    val route: String = destinationName.plus(args.joinToString { "/{${it.name}}" })

    fun createRoute(vararg args: String) = "$destinationName${args.joinToString { "/$it" }}"

    object BodyParts : Route("BodyParts")
    object Exercises : Route(
        "Exercises", listOf(
            navArgument("bodyPart") {
                type = NavType.StringType
            })
    )

    object ExerciseData : Route(
        "ExerciseData",
        listOf(
            navArgument("bodyPart") {
                type = NavType.StringType
            },
            navArgument("exercise") {
                type = NavType.StringType
            }
        ))
}