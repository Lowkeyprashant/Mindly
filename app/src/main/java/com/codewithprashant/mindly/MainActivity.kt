package com.codewithprashant.mindly

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.codewithprashant.mindly.ui.MindlyNavGraph
import com.codewithprashant.mindly.ui.theme.MindlyTheme
import dagger.hilt.android.AndroidEntryPoint
import androidx.core.view.WindowCompat

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        WindowCompat.setDecorFitsSystemWindows(window, true)
        setContent {
            MindlyTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    MindlyNavGraph()
                }
            }
        }
    }
}