package com.fexed.nuzlockone

import MainView
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import cacheFilesDir

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cacheFilesDir = cacheDir
        setContent {
            MainView()
        }
    }
}