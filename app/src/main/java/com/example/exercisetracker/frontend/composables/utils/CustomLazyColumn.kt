package com.example.exercisetracker.frontend.composables.utils

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun <T> CustomLazyColumn(
    modifier: Modifier = Modifier,
    data: SnapshotStateList<T>,
    onAddClick: (() -> Unit)? = null,
    item: @Composable LazyItemScope.(Int, T) -> Unit
) {
    LazyColumn(
        modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        itemsIndexed(data) {index, item ->
            item(index, item)
        }
        if (onAddClick != null) {
            item {
                AddButton(
                    Modifier
                        .fillMaxSize(),
                    onClick = onAddClick
                )
            }
        }

    }
}