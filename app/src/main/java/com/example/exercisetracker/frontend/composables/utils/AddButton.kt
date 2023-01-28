package com.example.exercisetracker.frontend.composables.utils

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.exercisetracker.R

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AddButton(
    modifier: Modifier = Modifier,
    onClick : () -> Unit
) {
    RoundWithBorders(
        modifier,
        40,
        contentPadding = 22.dp
    ) {
        Box(
            Modifier
                .fillMaxSize()

                .combinedClickable(
                    onClick = onClick
                    )
                ,
            contentAlignment = Alignment.Center
        ) {
            Image(painter = painterResource(id = R.drawable.add), contentDescription = "add")
        }
    }
}