package com.example.exercisetracker.frontend.composables.details

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.exercisetracker.R
import com.example.exercisetracker.backend.data.ExerciseDetails
import com.example.exercisetracker.frontend.composables.utils.RoundWithBorders
import java.text.SimpleDateFormat

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ExerciseDetailsItem(
    modifier: Modifier = Modifier,
    details: ExerciseDetails,
) {



    RoundWithBorders(modifier, roundCornerPercentage = 40) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Row(
                Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(painter = painterResource(id = R.drawable.dumbbell), contentDescription = "weight")
                    Text("${details.weight} kg")
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(painter = painterResource(id = R.drawable.repetitions), contentDescription = "repetitions")
                    Text(details.repetitions.toString())
                }
                Column(
                    Modifier.align(Alignment.CenterVertically)
                )
                {
                    Text(SimpleDateFormat.getDateInstance().format(details.timestamp))
                }
            }
        }
    }

}