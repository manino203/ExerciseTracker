package com.example.exercisetracker.frontend

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.exercisetracker.backend.viewmodels.MainViewModel
import com.example.exercisetracker.backend.viewmodels.ToolbarViewModel
import com.example.exercisetracker.frontend.composables.Toolbar
import com.example.exercisetracker.frontend.composables.bodyparts.BodyPartsScreen
import com.example.exercisetracker.frontend.composables.details.ExerciseDetailsScreen
import com.example.exercisetracker.frontend.composables.exercises.ExeriseScreen
import com.example.exercisetracker.frontend.composables.utils.routes.Route
import com.example.exercisetracker.ui.theme.ExerciseTrackerTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel: MainViewModel by viewModels()
        val toolbarViewModel: ToolbarViewModel by viewModels()
        setContent {
            AppContent(viewModel, toolbarViewModel)
        }
    }
}


@Composable
private fun AppContent(viewModel: MainViewModel, toolbarViewModel: ToolbarViewModel){
    val navController = rememberNavController()
     val context = LocalContext.current

    LaunchedEffect(viewModel.exportFileUri.value){
        viewModel.exportFileUri.value?.let {
            context.startActivity(
                Intent.createChooser(
                    Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_STREAM, it)
                        type = "*/*"
                        flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                    },
                    null
                )
            )
        }
    }

    ExerciseTrackerTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Scaffold(
                topBar = {
                    Toolbar(
                        uiState = toolbarViewModel.uiState.value,
                        navigateBack = {navController.popBackStack()},
                        importData = {},
                        exportData = {}
                    )
                },
            content = { padding ->
                Box(
                    Modifier.padding(padding)
                ){

                    NavHost(
                        navController = navController,
                        startDestination = Route.BodyParts.route
                    ) {

                        BodyPartsScreen(
                            toolbarViewModel = toolbarViewModel,
                            viewModel = viewModel,
                            navController = navController
                        )

                        ExeriseScreen(
                            viewModel =  viewModel,
                            toolbarViewModel = toolbarViewModel,
                            navController = navController
                        )

                        ExerciseDetailsScreen(
                            viewModel = viewModel,
                            toolbarViewModel = toolbarViewModel,
                        )

                    }
                }
            }
            )
        }
    }
}

@Preview
@Composable
fun AppPreview()
{
    val viewModel: MainViewModel by viewModel()
    val toolbarViewModel: ToolbarViewModel by viewModel()

    AppContent(viewModel = viewModel, toolbarViewModel)
}