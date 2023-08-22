package ui

import androidx.compose.runtime.MutableState
import data.Type

class FilterState private constructor() {
    lateinit var currentSelectedType: MutableState<Type>
    lateinit var currentSelectedGame: MutableState<Int>

    companion object {
        val instance: FilterState by lazy {
            FilterState()
        }
    }
}