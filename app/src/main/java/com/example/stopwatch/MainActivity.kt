package com.example.stopwatch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.os.SystemClock
import android.widget.Button
import android.widget.Chronometer

class MainActivity : AppCompatActivity() {
    lateinit var stopwatch:Chronometer
    var running = false
    var offset:Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        stopwatch = findViewById(R.id.stopwatch)
        if(savedInstanceState!=null){
            offset = savedInstanceState.getLong(OFFSET_KEY)
            running = savedInstanceState.getBoolean(RUNNING_KEY)
            if(running){
                stopwatch.base = savedInstanceState.getLong(BASE_KEY)
                stopwatch.start()
            }else setBaseTime()
        }
        val startButton = findViewById<Button>(R.id.start_button)
        startButton.setOnClickListener {
            if(!running){
                setBaseTime()
                stopwatch.start()
                running = true
            }
        }
        val pauseButton = findViewById<Button>(R.id.pause_button)
        pauseButton.setOnClickListener {
            if (running){
                saveOffset()
                stopwatch.stop()
                running = false
            }
        }
        val resetButton = findViewById<Button>(R.id.reset_button)
        resetButton.setOnClickListener {
            offset = 0
            setBaseTime()
        }
    }

    override fun onPause() {
        super.onPause()
        if(running){
            saveOffset()
            stopwatch.stop()
        }
    }

    override fun onResume() {
        super.onResume()
        if (running){
            setBaseTime()
            stopwatch.start()
            offset = 0
        }
    }

    private fun setBaseTime(){
        stopwatch.base = SystemClock.elapsedRealtime() - offset
    }

    private fun saveOffset(){
        offset = SystemClock.elapsedRealtime() - stopwatch.base
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        savedInstanceState.putLong(OFFSET_KEY,offset)
        savedInstanceState.putLong(BASE_KEY,stopwatch.base)
        savedInstanceState.putBoolean(RUNNING_KEY,running)
        super.onSaveInstanceState(savedInstanceState)
    }

    companion object{
        const val OFFSET_KEY = "offset"
        const val RUNNING_KEY = "running"
        const val BASE_KEY = "base"
    }
}