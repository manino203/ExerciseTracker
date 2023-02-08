package com.example.exercisetracker.frontend.composables.utils

class DialogFormDataList(
    vararg initialItems: DialogFormData,

    ) {

    private val defaultItemsValues = initialItems.map {
        it.state.value
    }.toList()
    val items: List<DialogFormData> = initialItems.toList()

    fun resetToDefaultValues() {
        items.forEachIndexed { index, item ->
            item.state.value = defaultItemsValues[index]
        }
    }


}