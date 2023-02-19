package com.example.exercisetracker.frontend.composables.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun Item(
    modifier: Modifier = Modifier,
    roundCornerPercentage: Int = 5,
    contentPadding: Dp = 16.dp,
    dragHandle: @Composable (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    val shape = RoundedCornerShape(roundCornerPercentage)

    Card(
        modifier
            ,
        shape = shape,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        border = CardDefaults.outlinedCardBorder(
            true
        )

    ) {
        Column(
            Modifier

                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ){
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(contentPadding),
                horizontalArrangement = Arrangement.Center
            ){ content() }
            Row(
                Modifier
                    .fillMaxWidth()

            ){ Divider(color = MaterialTheme.colorScheme.onSurface) }
            Row(
                Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background),
                horizontalArrangement = Arrangement.Center
            ){ dragHandle?.invoke() }
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
