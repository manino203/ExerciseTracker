package com.example.exercisetracker.frontend.composables.exercises

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.exercisetracker.backend.data.db.DataClassFactory
import com.example.exercisetracker.backend.data.db.entities.Exercise
import com.example.exercisetracker.backend.data.db.entities.ExerciseDetails
import com.example.exercisetracker.backend.viewmodels.ExercisesViewModel
import com.example.exercisetracker.backend.viewmodels.ToolbarViewModel
import com.example.exercisetracker.frontend.composables.Screen
import com.example.exercisetracker.frontend.composables.utils.ReorderableLazyColumn
import com.example.exercisetracker.frontend.composables.utils.dialogs.ExerciseDialogForm
import com.example.exercisetracker.frontend.composables.utils.dialogs.FormDialog
import com.example.exercisetracker.frontend.composables.utils.dialogs.rememberFormState
import com.example.exercisetracker.frontend.composables.utils.routes.Route


@Suppress("FunctionName")
fun NavGraphBuilder.ExerciseScreen(
    toolbarViewModel: ToolbarViewModel,
    navController: NavController,
){
    composable(
        Route.Exercises.route,
        Route.Exercises.args
    ) { navBackStackEntry ->
        val context = LocalContext.current
        val viewModel: ExercisesViewModel = hiltViewModel()
        val uiState = viewModel.uiState
        val bodyPartId by remember {
            mutableIntStateOf(
                navBackStackEntry.arguments?.getInt(
                    Route.Exercises.args[0].name
                )!!
            )
        }

        LaunchedEffect(Unit) {
            viewModel.updateTitle(bodyPartId) {
                toolbarViewModel.onScreenChange(Route.Exercises, context.getString(it))
            }
            viewModel.getExercises(bodyPartId)
        }

        LaunchedEffect(uiState.loading){
            toolbarViewModel.updateLoading(uiState.loading)
        }

        ExercisesScreen(
            exercises = uiState.exercises,
            bpId = bodyPartId,
            onEdit = { newName, exercise ->
                viewModel.editExercise(newName, exercise)
            },
            addItem = {
                viewModel.addExercise(it)
            },
            onItemClick = { exercise ->
                navController.navigate(
                    Route.ExerciseDetails.createRoute(
                        exercise.id.toString()
                    )
                )
            },
            onDelete = {
                viewModel.deleteExercise(it)
            },
            onSwap = { from: Int, to: Int ->
                viewModel.onExerciseMove(
                    from,
                    to
                )
            },
            onAccordionExpand = { exercise, loading, details ->
                viewModel.getDetailsForGraph(
                    exercise,
                    loading,
                    details
                )
            }
        )
    }
}

@Composable
private fun ExercisesScreen(
    exercises: List<Pair<Exercise, ExerciseDetails?>>,
    bpId: Int,
    onEdit: (String, Exercise) -> Boolean,
    addItem: (Exercise) -> Boolean,
    onItemClick: (Exercise) -> Unit,
    onDelete: (Exercise) -> Unit,
    onSwap: (Int, Int) -> Unit,
    onAccordionExpand: (Exercise, MutableState<Boolean>, MutableState<List<ExerciseDetails>>) -> Unit
) {

    val canExpand = remember {
        mutableStateOf(true)
    }

    val expandedStates = remember {
        mapOf<Int, MutableState<Boolean>>()
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
                addItem(DataClassFactory.createExercise(formState.values.map { it.value }, bpId))
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
            }
        ) { _, exercise, dragModifier, elevation ->

            expandedStates[exercise.first.id!!] = remember { mutableStateOf(false) }

            ExerciseItem(
                Modifier
                    .fillMaxWidth(),
                exercise,
                onClick = onItemClick,
                dragModifier = dragModifier,
                elevation = elevation,
                isExpanded = expandedStates[exercise.first.id]!!,
                canExpand = canExpand,
                onExpand = onAccordionExpand,
                editItem = onEdit,
                onDelete = onDelete
            )
        }
    }
}