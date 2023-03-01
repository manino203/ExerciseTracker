package com.example.exercisetracker.frontend.composables.exercises

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.exercisetracker.frontend.composables.utils.Item

@Composable
fun Accordion(
    modifier: Modifier = Modifier,
    isExpanded: Boolean = false,
    header: @Composable (Modifier) -> Unit,
    content: @Composable () -> Unit
) {


    Column(modifier) {
        header(
            Modifier
                .zIndex(1f)
                .shadow(if (isExpanded) 5.dp else 0.dp)
        )
        AnimatedVisibility(
            visible = isExpanded
        ) {
            Item(
                Modifier
                    .zIndex(0.5f)
                    .offset(0.dp, (-10).dp)
                    .fillMaxWidth()
                    .height(300.dp),
                roundCornerPercentage = 2,
                contentPadding = 0.dp
            ) {
                content()
            }
        }
    }
}