/*
 * Copyright 2022 André Claßen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.exercisetracker.frontend.composables.utils.reorderable

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.awaitLongPressOrCancellation
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput

//fun Modifier.detectReorder(state: ReorderableState<*>) =
//    this.then(
//        Modifier.pointerInput(Unit) {
//            detectVerticalDragGestures { change, dragAmount ->
//
//
//                state.interactions.trySend(StartDrag(PointerId(0L), Offset(dragAmount, 0f)))
//
//
//            }
//        }
//    )

fun Modifier.detectReorderAfterLongPress(state: ReorderableState<*>, onLongPress: () -> Unit) =
    this.then(
        Modifier.pointerInput(Unit) {
            awaitEachGesture {
                val down = awaitFirstDown(requireUnconsumed = false).also { it.consume() }
                awaitLongPressOrCancellation(down.id)?.also {
                    onLongPress.invoke()
                    state.interactions.trySend(StartDrag(down.id))
                }


            }
        }
    )