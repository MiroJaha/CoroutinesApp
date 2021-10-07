package com.example.coroutinesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import org.json.JSONObject
import java.lang.Exception
import java.net.URL

class MainActivity : AppCompatActivity() {

    private lateinit var tView: TextView
    private lateinit var button: Button
    private lateinit var button2: Button

    val adviceUrl = "https://api.adviceslip.com/advice"
    var response = ""
    var advice = ""
    var check = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tView = findViewById(R.id.tvMain)
        button = findViewById(R.id.button)
        button2 = findViewById(R.id.button2)

        val handler = Handler()

        button.setOnClickListener {
            reDo()
            if(check)
                handler.postDelayed({
                    button.performClick()
                }, 3000)
            check = true
        }

        button2.setOnClickListener {
            check = false
            handler.postDelayed({
                Toast.makeText(this,"onPause",Toast.LENGTH_LONG).show()
            }, 3000)
        }

    }

    private fun reDo() {
        CoroutineScope(IO).launch {
            try {
                response = URL(adviceUrl).readText(Charsets.UTF_8)

            } catch (e: Exception) {
            }

            var data = async { response }.await()
            if (data.isNotEmpty()) {
                withContext(Main) {
                    val jsonObject = JSONObject(data)
                    val slip = jsonObject.getJSONObject("slip")
                    advice = slip.getString("advice")
                    tView.text = advice
                }
            }
        }
    }

}