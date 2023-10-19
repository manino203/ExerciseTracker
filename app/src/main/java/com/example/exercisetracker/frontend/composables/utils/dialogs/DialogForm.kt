package com.example.exercisetracker.frontend.composables.utils.dialogs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import com.example.exercisetracker.R
import com.example.exercisetracker.backend.data.Exercise
import com.example.exercisetracker.backend.data.ExerciseDetails
import com.example.exercisetracker.frontend.composables.utils.DateFormatter
import java.time.Instant

typealias ResourceString = Int

open class DialogForm(
    val items: List<DialogFormData>,
){
    val values = items.map {
        it.state.value
    }
}

class DetailsDialogForm(
    details: ExerciseDetails?,
): DialogForm(
    listOf(
        DialogFormData(
            FormFieldFormat.Float,
            R.string.weight,
            mutableStateOf(details?.weight?.toString() ?: "")
        ),
        DialogFormData(
            FormFieldFormat.Int,
            R.string.reps,
            mutableStateOf(details?.reps?.toString() ?: "")
        ),
        DialogFormData(
            FormFieldFormat.Int,
            R.string.series,
            mutableStateOf(details?.series?.toString() ?: "")
        ),
        DialogFormData(
            FormFieldFormat.Date,
            R.string.date,
            mutableStateOf(DateFormatter.toDate(details?.timestamp ?: Instant.now().toEpochMilli()))
        )
    )
)

class ExerciseDialogForm(
    exercise: Exercise?,
): DialogForm(
   listOf(
       DialogFormData(
           FormFieldFormat.Str,
           R.string.name,
           mutableStateOf(exercise?.name ?: "")
       )
   )
)

data class FormState(
    val form: DialogForm,
    val values: List<MutableState<String>>
)

@Composable
fun rememberFormState(form: DialogForm): FormState {
    val values = form.values.map { rememberSaveable(it) { mutableStateOf(it) } }
    return FormState(
        form,
        values
    )
}