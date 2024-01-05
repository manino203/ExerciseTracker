package com.example.exercisetracker.frontend.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.exercisetracker.R
import com.example.exercisetracker.backend.viewmodels.ToolbarUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Toolbar(
    uiState: ToolbarUiState,
    navigateBack: () -> Unit,
    importData: () -> Unit,
    exportData: () -> Unit,
){
    Column {
        TopAppBar(
            navigationIcon = {
                AnimatedVisibility (
                    uiState.showBackButton,
                    enter = EnterTransition.None,
                    exit = ExitTransition.None
                ) {
                    IconButton(onClick = navigateBack) {
                        Box(contentAlignment = Alignment.TopEnd) {
                            Icon(Icons.Default.ArrowBack, contentDescription = null)
                        }
                    }
                }
            },
            title = {
                Text(uiState.title)
            },
            actions = {
                Box{
                    var expanded by remember{
                        mutableStateOf(false)
                    }
                    IconButton(onClick = { expanded = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = null)
                    }

                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false}) {
                        DropdownMenuItem(
                            text = { Text(stringResource(id = R.string.export_data)) },
                            onClick = {
                                expanded = false
                                exportData()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(stringResource(id = R.string.import_data)) },
                            onClick = {
                                expanded = false
                                importData()
                            }
                        )
                    }
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
        )
        AnimatedVisibility (uiState.loading) {
            LinearProgressIndicator(Modifier.fillMaxWidth())
        }
    }
}