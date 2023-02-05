package com.example.exercisetracker.frontend.composables.utils

class EditDataList(
    vararg initialItems: EditDataWrapper
) {

    private val defaultItemsValues = initialItems.map {
        it.state.value
    }.toList()
    val items: List<EditDataWrapper> = initialItems.toList()

    fun resetToDefaultValues() {
        items.forEachIndexed { index, item ->
            item.state.value = defaultItemsValues[index]
        }
    }


}