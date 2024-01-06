package com.example.exercisetracker.frontend

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.exercisetracker.R
import com.example.exercisetracker.backend.viewmodels.MainViewModel
import com.example.exercisetracker.backend.viewmodels.ToolbarViewModel
import com.example.exercisetracker.frontend.composables.Toolbar
import com.example.exercisetracker.frontend.composables.bodyparts.BodyPartsScreen
import com.example.exercisetracker.frontend.composables.details.ExerciseDetailsScreen
import com.example.exercisetracker.frontend.composables.exercises.ExerciseScreen
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
    val importFailMessage = stringResource(id = R.string.import_fail)

    val importActivityLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = {res ->
            res.data?.data?.let{
                viewModel.importData(it, importFailMessage)
                (context as Activity).recreate()
            }
        }
    )
    val exportActivityLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { _ -> }
    )


    LaunchedEffect(viewModel.isLoading.value){
        toolbarViewModel.updateLoading(viewModel.isLoading.value)
    }

    LaunchedEffect(Unit){
        viewModel.toastMessage.collect{
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }

    ExerciseTrackerTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Scaffold(
                topBar = {
                    val exportFailMessage = stringResource(id = R.string.export_fail)
                    Toolbar(
                        uiState = toolbarViewModel.uiState.value,
                        navigateBack = {navController.popBackStack()},
                        importData = {
                            importActivityLauncher.launch(
                                Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                                    addCategory(Intent.CATEGORY_OPENABLE)
                                    type = "text/plain"
                                    putExtra(
                                        DocumentsContract.EXTRA_INITIAL_URI,
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                            MediaStore.Downloads.EXTERNAL_CONTENT_URI
                                        } else {
                                            Uri.fromFile(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS))
                                        }
                                    )
                                }
                            )
                         },
                        exportData = {
                            viewModel.exportData(exportFailMessage) {
                                exportActivityLauncher.launch(
                                    Intent().apply {
                                        action = Intent.ACTION_SEND
                                        putExtra(Intent.EXTRA_STREAM, it)
                                        type = "text/plain"
                                        flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                                    },
                                    null
                                )
                            }
                        }
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
                                navController = navController
                            )

                            ExerciseScreen(
                                toolbarViewModel = toolbarViewModel,
                                navController = navController,
                            )

                            ExerciseDetailsScreen(
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
    val viewModel: MainViewModel = hiltViewModel()
    val toolbarViewModel: ToolbarViewModel = hiltViewModel()

    AppContent(viewModel = viewModel, toolbarViewModel)
}