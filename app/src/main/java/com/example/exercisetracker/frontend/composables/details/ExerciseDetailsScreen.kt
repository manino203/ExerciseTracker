package com.example.exercisetracker.frontend.composables.details

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.exercisetracker.R
import com.example.exercisetracker.backend.data.DataClassFactory
import com.example.exercisetracker.backend.data.ExerciseDetails
import com.example.exercisetracker.backend.data.Path
import com.example.exercisetracker.backend.viewmodels.DetailsViewModel
import com.example.exercisetracker.backend.viewmodels.ToolbarViewModel
import com.example.exercisetracker.frontend.composables.Screen
import com.example.exercisetracker.frontend.composables.utils.DateFormatter.Companion.dateFormat
import com.example.exercisetracker.frontend.composables.utils.ReorderableLazyColumn
import com.example.exercisetracker.frontend.composables.utils.dialogs.CalendarDialog
import com.example.exercisetracker.frontend.composables.utils.dialogs.DetailsDialogForm
import com.example.exercisetracker.frontend.composables.utils.dialogs.FormDialog
import com.example.exercisetracker.frontend.composables.utils.dialogs.rememberFormState
import com.example.exercisetracker.frontend.composables.utils.routes.Route
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Locale


@Suppress("FunctionName")
fun NavGraphBuilder.ExerciseDetailsScreen(
    toolbarViewModel: ToolbarViewModel,
){
    composable(
        Route.ExerciseDetails.route,
        Route.ExerciseDetails.args
    ) {
        val viewModel: DetailsViewModel = hiltViewModel()
        val bodyPart by remember { mutableStateOf(it.arguments?.getString(Route.ExerciseDetails.args[0].name)!!) }
        val exercise by remember { mutableStateOf(it.arguments?.getString(Route.ExerciseDetails.args[1].name)!!) }
        val path by remember {
            mutableStateOf(Path(bodyPart, exercise))
        }
        val appName = stringResource(id = R.string.app_name)
        LaunchedEffect(Unit){
            toolbarViewModel.onScreenChange(Route.ExerciseDetails, it.arguments?.getString(Route.ExerciseDetails.args[2].name) ?: appName)
            viewModel.getDetails(
                Path(
                    bodyPart,
                    exercise
                )
            )
        }

        LaunchedEffect(viewModel.isLoading.value){
            toolbarViewModel.updateLoading(viewModel.isLoading.value)
        }

        ExerciseDetailsScreen(
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ExerciseDetailsScreen(
    detailsList: SnapshotStateList<ExerciseDetails>,
    addItem: (ExerciseDetails) -> Unit,
    editItem: (ExerciseDetails, Int) -> Unit,
    deleteItem: (Int) -> Unit
) {

    val calendarState = rememberDatePickerState(Instant.now().toEpochMilli())


    val formState = rememberFormState(form = DetailsDialogForm(null))

    var dialogOpen by remember {
        mutableStateOf(false)
    }

    var calendarOpen by remember {
        mutableStateOf(false)
    }

    if (dialogOpen) {
        FormDialog(
            formState = formState,
            onCalendarClick = {
                calendarOpen = true
            },
            onDismiss = { dialogOpen = false }
        ) {
            addItem(
                DataClassFactory.createExerciseDetails(formState.values.map { it.value })
            )
        }
    }
    if (calendarOpen) {
        CalendarDialog(
            calendarState = calendarState,
            onDismiss = {
                calendarOpen = false
            },
            onOkClick = {
                formState.values[3].value =
                    SimpleDateFormat(dateFormat, Locale.getDefault()).format(
                        calendarState.selectedDateMillis ?: 0
                    )
                calendarOpen = false
            },
            onCancelClick = {
                calendarOpen = false
            }
        )
    }
    Screen(
        onAddClick = {
            dialogOpen = true
        },
    ){

        ReorderableLazyColumn(
            Modifier.zIndex(0.5f),
            data = detailsList,
        )
        { index, details, _, elevation ->
            ExerciseDetailsItem(
                Modifier
                    .fillMaxWidth(),
                details,
                index,
                elevation,
                deleteItem,
                editItem
            )
        }
    }
}