package com.example.exercisetracker.frontend.composables.utils

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.exercisetracker.R

@Composable
fun Item(
    modifier: Modifier = Modifier,
    contentModifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    roundCornerPercentage: Int = 5,
    contentPadding: Dp = 16.dp,
    dragModifier: Modifier? = null,
    elevation: State<Dp> = mutableStateOf(0.dp),
    content: @Composable () -> Unit
) {
    val shape = RoundedCornerShape(roundCornerPercentage)

    Card(
        modifier,
        backgroundColor = backgroundColor,
        shape = shape,
        elevation = elevation.value,
        border = BorderStroke(1.dp, Color(255, 30, 90))

    ) {
        Column(
            Modifier

                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(contentModifier) {
                Row(
                    Modifier
                        .fillMaxSize()
                        .padding(contentPadding),
                    horizontalArrangement = Arrangement.Center
                ) { content() }
            }

            dragModifier?.let {
                return@let Divider(
                    color = Color(255, 0, 60)
                )
            }

            dragModifier?.let {
                return@let Row(
                    Modifier
                        .fillMaxWidth()
                        .background(Color.Black)
                        .then(dragModifier),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Image(
                        painterResource(id = R.drawable.drag_handle),
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
                        contentDescription = "",
                    )
                }
            }
        }

    }
}

@Preview
@Composable
fun ItemPreview() {
    Item(
        Modifier.fillMaxSize(),
        elevation = remember { mutableStateOf(10.dp) }
    ) {
        Text("wat")
//        Column(Modifier.fillMaxSize()){
//            Text(text = "test")
//            Divider()
//            Image(painter = painterResource(id = R.drawable.drag_handle), contentDescription = "")
//        }
    }
}
