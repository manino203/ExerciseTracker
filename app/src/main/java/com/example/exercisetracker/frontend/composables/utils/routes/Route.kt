package com.example.exercisetracker.frontend.composables.utils.routes

import android.util.Log
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class Route(private val destinationName: String, val args: List<NamedNavArgument> = listOf()) {

    val route: String = destinationName.plus(args.joinToString { "/{${it.name}}" })

    fun createRoute(vararg args: String): String{
        Log.d("createRoute", "$destinationName${args.joinToString { "/$it" }}")
        return "$destinationName${args.joinToString { "/$it" }}"
    }

    data object BodyParts : Route("BodyParts")
    data object Exercises : Route(
        "Exercises", listOf(
            navArgument("bodyPartId") {
                type = NavType.IntType
            }
        )
    )

    data object ExerciseDetails : Route(
        "ExerciseData",
        listOf(
            navArgument("exerciseId") {
                type = NavType.IntType
            }
        )
    )
}