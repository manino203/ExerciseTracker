package com.example.exercisetracker.frontend.composables.utils

import android.util.Log
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.exercisetracker.frontend.composables.utils.reorderable.ReorderableItem
import com.example.exercisetracker.frontend.composables.utils.reorderable.detectReorderAfterLongPress
import com.example.exercisetracker.frontend.composables.utils.reorderable.rememberReorderableLazyListState
import com.example.exercisetracker.frontend.composables.utils.reorderable.reorderable

@Composable
fun <T> ReorderableLazyColumn(
    modifier: Modifier = Modifier,
    data: List<T>,
    onAddClick: (() -> Unit)? = null,
    onSwap: ((Int, Int) -> Unit)? = null,
    onDragEnd: ((startIndex: Int, endIndex: Int) -> (Unit))? = null,
    onDragStart: (() -> (Unit))? = null,
    itemContent: @Composable LazyItemScope.(
        index: Int,
        item: T,
        dragHandle: @Composable (() -> Unit)?
    ) -> Unit
) {
    val state = rememberReorderableLazyListState(
        onMove = { from, to ->
            onSwap?.invoke(from.index, to.index)
        },
        onDragEnd = onDragEnd
    )

    val haptic = LocalHapticFeedback.current
    Column(
        Modifier
            .fillMaxSize()

            .padding(PaddingValues(16.dp, 8.dp, 16.dp, 0.dp)),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (onAddClick != null) {
            AddButton(
                Modifier
                    .fillMaxWidth(),
                onClick = onAddClick
            )

        }
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .then(
                    onSwap?.let {
                        return@let Modifier.reorderable(state)
                    } ?: Modifier
                ),
            state = state.listState,
//            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(items = data) { index, item ->
                ReorderableItem(
                    state,
                    item,
                    index = index
                ) { isDragging ->
                    val elevation by animateDpAsState(if (isDragging) 4.dp else 0.dp)
                    Column(
                        modifier = Modifier
                            .shadow(elevation)
                            .background(MaterialTheme.colorScheme.surface)
                    ) {
                        itemContent(
                            index,
                            item
                        ) {
                            Image(
                                painterResource(id = com.example.exercisetracker.R.drawable.drag_handle),
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
                                contentDescription = "",
                                modifier = Modifier
                                    .pointerInput(Unit) {
                                        detectTapGestures(
                                            onLongPress = {
                                                Log.d("longpress", " longpress")

                                            }
                                        )
                                    }
                                    .detectReorderAfterLongPress(state) {
                                        haptic.performHapticFeedback(
                                            HapticFeedbackType.LongPress
                                        )
                                        onDragStart?.invoke()
                                    }
                            )
                        }

                    }
                }
            }


        }
    }
}