package com.example.exercisetracker.frontend.composables.exercises

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.exercisetracker.R
import com.example.exercisetracker.frontend.composables.utils.RoundWithBorders

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ExerciseItem(
    modifier: Modifier = Modifier,
    label: String,
    onClick: (String) -> Unit
) {
    RoundWithBorders(modifier, roundCornerPercentage = 40){
        Box(
            modifier = Modifier
                .fillMaxSize()
                .combinedClickable(
                    onClick = { onClick(label) }
                ),
            contentAlignment = Alignment.Center
        ) {
            Row(
                Modifier
                    .fillMaxSize()
                    .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
            ){
                Column(Modifier.align(Alignment.CenterVertically),
                    verticalArrangement = Arrangement.SpaceAround
                ) {
                    Text(text = label)
                }
                   Column(horizontalAlignment = Alignment.CenterHorizontally){
                       Image(painterResource(id = R.drawable.dumbbell), contentDescription = "dumbbell")
                       Text("20 kg")
                   }
                   Column(
                       Modifier
                           .padding(8.dp, 0.dp),
                       horizontalAlignment = Alignment.CenterHorizontally
                   ){
                       Image(painterResource(id = R.drawable.repetitions), contentDescription = "repetitions")
                       Text("10")
                   } 

            }
        }
    }
}