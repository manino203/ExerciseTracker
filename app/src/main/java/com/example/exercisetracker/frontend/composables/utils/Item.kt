package com.example.exercisetracker.frontend.composables.utils

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun Item(
    modifier: Modifier = Modifier,
    roundCornerPercentage: Int = 5,
    contentPadding: Dp = 16.dp,
    content: @Composable () -> Unit
) {
    val shape = RoundedCornerShape(roundCornerPercentage)

    Card(
        modifier,
        shape = shape,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
//        border = CardDefaults.outlinedCardBorder(
//            true
//        )

    ) {
        Box(
            Modifier.padding(contentPadding)
        )
        {
            content()
        }
    }
}

@Preview
@Composable
fun ItemPreview() {
    Item(
        Modifier.fillMaxSize()
    ) {
        Text("wat")
//        Column(Modifier.fillMaxSize()){
//            Text(text = "test")
//            Divider()
//            Image(painter = painterResource(id = R.drawable.drag_handle), contentDescription = "")
//        }
    }
}
