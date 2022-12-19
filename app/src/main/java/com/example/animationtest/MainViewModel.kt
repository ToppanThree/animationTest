package com.example.animationtest

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {

    var isCurrentlyDragging by mutableStateOf(false)
        private set

    fun startDragging(){
        isCurrentlyDragging = true
    }
    fun stopDragging(){
        isCurrentlyDragging = false
    }
}