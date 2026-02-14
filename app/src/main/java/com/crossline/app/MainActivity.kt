package com.crossline.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.crossline.app.ui.dashboard.DummyMemoScreen
import com.crossline.app.ui.theme.CrossLineTheme
import com.crossline.app.util.FlipDetector
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private var flipDetector: FlipDetector? = null
    private var emergencySwitch: (() -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CrossLineTheme {
                var unlocked by rememberSaveable { mutableStateOf(false) }

                if (unlocked) {
                    CrossLineNavHost(
                        onEmergencySwitchReady = { callback ->
                            emergencySwitch = callback
                        }
                    )
                } else {
                    DummyMemoScreen(
                        onSecretGesture = { unlocked = true }
                    )
                }
            }
        }

        // Initialize flip detector for emergency switch
        flipDetector = FlipDetector(this) {
            emergencySwitch?.invoke()
        }
    }

    override fun onResume() {
        super.onResume()
        flipDetector?.start()
    }

    override fun onPause() {
        super.onPause()
        flipDetector?.stop()
    }
}
