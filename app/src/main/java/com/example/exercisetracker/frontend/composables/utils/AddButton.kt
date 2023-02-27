package com.example.exercisetracker.frontend.composables.utils

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.exercisetracker.R

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AddButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Column(
        modifier
            .combinedClickable(
                onClick = onClick
            )
    ) {
        Surface(
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
            color = Color.Transparent,
//        40,
//        contentPadding = PaddingValues(22.dp)
        ) {
            Box(
                Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.add),
                    contentDescription = stringResource(id = R.string.plus),
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground)
                )
            }
        }
        Divider()
    }
}