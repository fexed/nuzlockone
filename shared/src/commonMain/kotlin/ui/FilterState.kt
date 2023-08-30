package ui

import androidx.compose.runtime.MutableState
import data.NuzlockRun
import data.Type

class FilterState private constructor() {
    lateinit var currentSelectedType: MutableState<Type>
    lateinit var currentSelectedGame: MutableState<Int>
    lateinit var currentSelectedNuzlocke: MutableState<NuzlockRun?>
    lateinit var currentSearchString: MutableState<String>
    companion object {
        val instance: FilterState by lazy {
            FilterState()
        }
    }
}