package com.my.blog.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import java.io.File
import java.io.FileOutputStream
import java.util.Timer
import java.util.TimerTask

class MyViewModel : ViewModel() {

    private val stateFlow1 = MutableStateFlow(0)
    var stateFlow2 = stateFlow1.asStateFlow()

    fun startTimer() {
        Timer().scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                stateFlow1.value++
            }
        }, 0, 1000)
    }

    private val timeFlow = flow<Int> {
        var time = 0
        while (true) {
            emit(time)
            delay(1000)
            time++

        }
    }

    val stateFlow =
        timeFlow.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000, 5000), -10)

    fun test(){
        val file = File("")
        val fileOutputStream = FileOutputStream(file)
        fileOutputStream.use {
            file.setReadOnly()

        }
    }
}