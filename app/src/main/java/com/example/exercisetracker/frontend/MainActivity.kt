package com.example.exercisetracker.frontend

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.exercisetracker.backend.viewmodels.MainViewModel
import com.example.exercisetracker.frontend.composables.bodyparts.BodyPartsScreen
import com.example.exercisetracker.frontend.composables.details.ExerciseDetailsScreen
import com.example.exercisetracker.frontend.composables.exercises.ExercisesScreen
import com.example.exercisetracker.frontend.routes.Route
import com.example.exercisetracker.ui.theme.ExerciseTrackerTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel: MainViewModel by viewModels()
        setContent {
            val navController = rememberNavController()
            ExerciseTrackerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = Route.BodyParts.route
                    ) {

                        composable(Route.BodyParts.route) {

                            BodyPartsScreen(bodyParts = viewModel.bodyPartNames, onItemClick = {
                                viewModel.getExercises(it)
                                navController.navigate(Route.Exercises.createRoute(it))
                            })
                        }

                        //EXERCISES

                        composable(
                            Route.Exercises.route,
                            Route.Exercises.args
                        ) {
                            val bodyPart by remember { mutableStateOf(it.arguments?.getString(Route.Exercises.args[0].name)!!) }

                            ExercisesScreen(
                                loading = viewModel.exercisesLoading,
                                exercises = viewModel.exercises,
                                onAddClick = {
                                    viewModel.addExercise(bodyPart, "test")
                                },
                                onItemClick = { exercise ->
                                    viewModel.exerciseItemOnClick(
                                        Route.Exercises.createPath(
                                            bodyPart,
                                            exercise
                                        )
                                    )
                                    navController.navigate(
                                        Route.ExerciseData.createRoute(
                                            bodyPart,
                                            exercise
                                        )
                                    )
                                }
                            )
                        }

                        //DETAILS

                        composable(
                            Route.ExerciseData.route,
                            Route.ExerciseData.args
                        ) {
                            val bodyPart by remember { mutableStateOf(it.arguments?.getString(Route.ExerciseData.args[0].name)!!) }
                            val exercise by remember { mutableStateOf(it.arguments?.getString(Route.ExerciseData.args[1].name)!!) }

                            ExerciseDetailsScreen(
                                loading = viewModel.detailsLoading,
                                detailsList = viewModel.details
                            ) {

                                viewModel.addDetail(
                                    Route.ExerciseData.createPath(bodyPart, exercise),
                                    15f,
                                    5,
                                    0
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ExerciseTrackerTheme {
        Greeting("Android")
    }
}