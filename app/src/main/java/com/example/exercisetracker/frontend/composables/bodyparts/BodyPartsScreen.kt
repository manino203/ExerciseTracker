package com.example.exercisetracker.frontend.composables.bodyparts

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.exercisetracker.R
import com.example.exercisetracker.backend.data.BodyPart
import com.example.exercisetracker.backend.viewmodels.MainViewModel
import com.example.exercisetracker.backend.viewmodels.ToolbarViewModel
import com.example.exercisetracker.frontend.composables.Screen
import com.example.exercisetracker.frontend.composables.utils.ReorderableLazyColumn
import com.example.exercisetracker.frontend.composables.utils.routes.Route

@Suppress("FunctionName")
fun NavGraphBuilder.BodyPartsScreen(
    toolbarViewModel: ToolbarViewModel,
    viewModel: MainViewModel,
    navController: NavController
){
    composable(Route.BodyParts.route) {
//        val viewModel: BodyPartsViewModel = hiltViewModel()
        val appName = stringResource(id = R.string.app_name)
        LaunchedEffect(Unit) {
            toolbarViewModel.onScreenChange(Route.BodyParts, appName)
            viewModel.getBodyParts()

        }

        LaunchedEffect(viewModel.bodyPartsLoading.value){
            toolbarViewModel.updateLoading(viewModel.bodyPartsLoading.value)
            viewModel.bodyParts.forEach {
                Log.d("composable: ${it.path}", "${ it.label }")
            }
        }

        BodyPartsScreen(
            bodyParts = viewModel.bodyParts,
            onItemClick = {
//                viewModel.getExercises(it)
                navController.navigate(Route.Exercises.createRoute(it))
            },
            onSwap = { from: Int, to: Int ->
                viewModel.onBodyPartMove(from, to)
            },
            onDragEnd = {

            }
        )
    }

}

@Composable
private fun BodyPartsScreen(
    bodyParts: List<BodyPart>,
    onItemClick: (String) -> Unit,
    onSwap: (Int, Int) -> Unit,
    onDragEnd: () -> Unit,
) {
    Screen(
        onAddClick = null
    ){
        ReorderableLazyColumn(
            data = bodyParts,
            onSwap = onSwap,
            onDragEnd = { _, _ ->
                onDragEnd()
            }
        ) { _, bp, dh, elevation ->

            BodyPartItem(
                label = stringResource(bp.label),
                dragModifier = dh,
                elevation = elevation
            ) {
                onItemClick(bp.path)
            }
        }
    }
}