package com.example.exercisetracker.frontend.composables.utils

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.Dp
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
        dragModifier: Modifier?,
        elevation: State<Dp>
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
                    Modifier.background(Color.Transparent),
                    index = index
                ) { isDragging ->
                    val elevation = animateDpAsState(if (isDragging) 10.dp else 0.dp)
                    Column(
                        modifier = Modifier
//                            .shadow(elevation)
                            .background(Color.Transparent)
                    ) {
                        itemContent(
                            index,
                            item,
                            Modifier.detectReorderAfterLongPress(state) {
                                haptic.performHapticFeedback(
                                    HapticFeedbackType.LongPress
                                )
                                onDragStart?.invoke()
                            },
                            elevation

                        )
                    }
                }


            }
        }
    }
}