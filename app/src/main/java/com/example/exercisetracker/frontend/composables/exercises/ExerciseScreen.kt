package com.example.exercisetracker.frontend.composables.exercises

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.exercisetracker.R
import com.example.exercisetracker.backend.data.DataClassFactory
import com.example.exercisetracker.backend.data.Exercise
import com.example.exercisetracker.backend.data.ExerciseDetails
import com.example.exercisetracker.backend.data.Path
import com.example.exercisetracker.backend.viewmodels.MainViewModel
import com.example.exercisetracker.backend.viewmodels.ToolbarViewModel
import com.example.exercisetracker.frontend.composables.Screen
import com.example.exercisetracker.frontend.composables.utils.ReorderableLazyColumn
import com.example.exercisetracker.frontend.composables.utils.dialogs.ExerciseDialogForm
import com.example.exercisetracker.frontend.composables.utils.dialogs.FormDialog
import com.example.exercisetracker.frontend.composables.utils.dialogs.rememberFormState
import com.example.exercisetracker.frontend.composables.utils.routes.Route


@Suppress("FunctionName")
fun NavGraphBuilder.ExeriseScreen(
    viewModel: MainViewModel,
    toolbarViewModel: ToolbarViewModel,
    navController: NavController
){
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

        val title = stringResource(
            viewModel.getBodyPartByPath(bodyPartPath)?.label ?: R.string.app_name
        )

        LaunchedEffect(Unit) {
            toolbarViewModel.onScreenChange(Route.Exercises, title)
        }

        LaunchedEffect(viewModel.exercisesLoading.value){
            toolbarViewModel.updateLoading(viewModel.exercisesLoading.value)
        }

        ExercisesScreen(
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
                    Route.ExerciseDetails.createRoute(
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
}

@Composable
private fun ExercisesScreen(
    exercises: SnapshotStateList<Exercise>,
    bodyPartPath: String,
    onEdit: (String, Exercise) -> Unit,
    addItem: (Exercise) -> Unit,
    onItemClick: (Exercise) -> Unit,
    onDelete: (Exercise) -> Unit,
    onSwap: (Int, Int) -> Unit,
    onDragEnd: () -> Unit,
    onAccordionExpand: (Exercise, MutableState<Boolean>, SnapshotStateList<ExerciseDetails>) -> Unit
) {

    val canExpand = remember {
        mutableStateOf(true)
    }

    val expandedStates = remember {
        mapOf<String, MutableState<Boolean>>()
    }.toMutableMap()

    var dialogOpen by remember {
        mutableStateOf(false)
    }

    val formState = rememberFormState(form = ExerciseDialogForm(null))

    if (dialogOpen) {
        FormDialog(
            formState = formState,
            onDismiss = {
                dialogOpen = false
            },
            onConfirm = {
                addItem(DataClassFactory.createExercise(formState.values.map { it.value }, bodyPartPath))
            }
        )
    }
    Screen(
        onAddClick = {dialogOpen = true},
    ){
        ReorderableLazyColumn(
            Modifier
                .zIndex(0.5f),
            data = exercises,
            onSwap = { from, to ->
                onSwap(from, to)
            },
            onDragStart = {
                expandedStates.forEach {
                    it.value.value = false
                }
                canExpand.value = false
            },
            onDragEnd = { _, _ ->
                canExpand.value = true
                onDragEnd()
            }
        ) { _, exercise, dragModifier, elevation ->

            expandedStates[exercise.id] = remember { mutableStateOf(false) }

            ExerciseItem(
                Modifier
                    .fillMaxWidth(),
                exercise,
                onClick = onItemClick,
                dragModifier = dragModifier,
                elevation = elevation,
                isExpanded = expandedStates[exercise.id]!!,
                canExpand = canExpand,
                onExpand = onAccordionExpand,
                editItem = onEdit,
                onDelete = onDelete
            )
        }
    }
}