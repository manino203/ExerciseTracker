package com.example.exercisetracker.frontend

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.exercisetracker.backend.data.Path
import com.example.exercisetracker.backend.viewmodels.MainViewModel
import com.example.exercisetracker.frontend.composables.bodyparts.BodyPartsScreen
import com.example.exercisetracker.frontend.composables.details.ExerciseDetailsScreen
import com.example.exercisetracker.frontend.composables.exercises.ExercisesScreen
import com.example.exercisetracker.frontend.composables.utils.routes.Route
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
                            LaunchedEffect(Unit) {
                                viewModel.getBodyParts()
                            }
                            BodyPartsScreen(
                                bodyParts = viewModel.bodyParts,
                                onItemClick = {
                                    viewModel.getExercises(it)
                                    navController.navigate(Route.Exercises.createRoute(it))
                                },
                                onSwap = { from: Int, to: Int ->
                                    viewModel.onBodyPartMove(from, to)
                                },
                                onDragEnd = {

                                }
                            )
                        }

                        //EXERCISES

                        composable(
                            Route.Exercises.route,
                            Route.Exercises.args
                        ) {
                            val bodyPartPath by remember {
                                mutableStateOf(
                                    it.arguments?.getString(
                                        Route.Exercises.args[0].name
                                    )!!
                                )
                            }

                            ExercisesScreen(
                                loading = viewModel.exercisesLoading.value,
                                exercises = viewModel.exercises,
                                bodyPartPath = bodyPartPath,
                                onEdit = { newName, exercise ->
                                    viewModel.editExercise(newName, exercise)
                                },
                                addItem = {
                                    viewModel.addExercise(it)
                                },
                                onItemClick = { exercise ->
                                    viewModel.getDetails(
                                        Path(
                                            bodyPartPath,
                                            exercise.id
                                        )
                                    )
                                    navController.navigate(
                                        Route.ExerciseData.createRoute(
                                            bodyPartPath,
                                            exercise.id
                                        )
                                    )
                                },
                                onDelete = {
                                    viewModel.deleteExercise(it)
                                },
                                onSwap = { from: Int, to: Int ->
                                    viewModel.onExerciseMove(
                                        bodyPartPath,
                                        from,
                                        to
                                    )
                                },
                                onDragEnd = {
                                    viewModel.saveExercises(bodyPartPath)
                                },
                                onAccordionExpand = { exercise, loading, details ->
                                    viewModel.getDetails(
                                        Path(bodyPartPath, exercise.id),
                                        loading,
                                        details
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
                            val path by remember {
                                mutableStateOf(Path(bodyPart, exercise))
                            }
                            ExerciseDetailsScreen(
                                loading = viewModel.detailsLoading.value,
                                detailsList = viewModel.details,
                                addItem = {
                                    viewModel.addDetail(path, it)
                                },
                                editItem = { detail, index ->
                                    viewModel.editDetail(detail, index, path)
                                }
                            ) {
                                viewModel.deleteDetail(it, path)
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