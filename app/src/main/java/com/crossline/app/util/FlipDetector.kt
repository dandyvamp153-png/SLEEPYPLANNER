package com.crossline.app.util

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager

/**
 * Detects when phone is flipped face-down using accelerometer.
 * Triggers emergency switch to MoneyTrack dashboard.
 *
 * Face-down = Z-axis acceleration < -9.0 (gravity pointing up = phone screen facing ground)
 */
class FlipDetector(
    context: Context,
    private val onFlipDetected: () -> Unit
) : SensorEventListener {

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    private var lastFlipTime = 0L

    fun start() {
        accelerometer?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    fun stop() {
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event ?: return
        if (event.sensor.type != Sensor.TYPE_ACCELEROMETER) return

        val z = event.values[2]
        val now = System.currentTimeMillis()

        // Phone is face-down when Z acceleration is strongly negative
        // Debounce: only trigger once per 2 seconds
        if (z < -9.0f && now - lastFlipTime > 2000) {
            lastFlipTime = now
            onFlipDetected()
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}
