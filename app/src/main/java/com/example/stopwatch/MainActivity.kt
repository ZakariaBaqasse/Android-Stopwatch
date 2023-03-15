package com.example.stopwatch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.os.SystemClock
import android.widget.Button
import android.widget.Chronometer
import com.example.stopwatch.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    var running = false
    var offset:Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        if(savedInstanceState!=null){
            offset = savedInstanceState.getLong(OFFSET_KEY)
            running = savedInstanceState.getBoolean(RUNNING_KEY)
            if(running){
                binding.stopwatch.base = savedInstanceState.getLong(BASE_KEY)
                binding.stopwatch.start()
            }else setBaseTime()
        }
        binding.startButton.setOnClickListener {
            if(!running){
                setBaseTime()
                binding.stopwatch.start()
                running = true
            }
        }
        binding.pauseButton.setOnClickListener {
            if (running){
                saveOffset()
                binding.stopwatch.stop()
                running = false
            }
        }
        binding.resetButton.setOnClickListener {
            offset = 0
            setBaseTime()
        }
    }

    override fun onPause() {
        super.onPause()
        if(running){
            saveOffset()
            binding.stopwatch.stop()
        }
    }

    override fun onResume() {
        super.onResume()
        if (running){
            setBaseTime()
            binding.stopwatch.start()
            offset = 0
        }
    }

    private fun setBaseTime(){
        binding.stopwatch.base = SystemClock.elapsedRealtime() - offset
    }

    private fun saveOffset(){
        offset = SystemClock.elapsedRealtime() - binding.stopwatch.base
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        savedInstanceState.putLong(OFFSET_KEY,offset)
        savedInstanceState.putLong(BASE_KEY,binding.stopwatch.base)
        savedInstanceState.putBoolean(RUNNING_KEY,running)
        super.onSaveInstanceState(savedInstanceState)
    }

    companion object{
        const val OFFSET_KEY = "offset"
        const val RUNNING_KEY = "running"
        const val BASE_KEY = "base"
    }
}