package com.example.exercisetracker.frontend.composables.utils

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun RoundWithBorders(
    modifier: Modifier = Modifier,
    roundCornerPercentage: Int = 5,
    borderWidth: Dp = 2.dp,
    contentPadding: Dp = 16.dp,
    borderColor: Color = Color.Black,
    content: @Composable () -> Unit
){
    val shape = RoundedCornerShape(roundCornerPercentage)
    Box(
        Modifier
            .then(modifier)
            .clip(shape)
            .border(borderWidth, borderColor, shape = shape),
        contentAlignment = Alignment.Center
    ){
        Box(
            Modifier.padding(contentPadding)
        )
        {
            content()
        }
    }
}