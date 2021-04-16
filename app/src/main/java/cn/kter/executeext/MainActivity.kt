package cn.kter.executeext

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.IOException
import java.lang.IllegalStateException

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        lifecycleScope.launch {
            suspend {
                delay(5000)
                throw IllegalStateException("IllegalStateException")
                "abc"
            } execute {
                Log.d("ExeExt", it)
            } filter {
                arrayOf(IOException::class.java)
            } then {
                Log.d("ExeExt", "${it.toString()}")
            } filter {
                arrayOf(IllegalStateException::class.java)
            } then {
                Log.d("ExeExt", "${it.toString()}")
            }
        }

    }
}