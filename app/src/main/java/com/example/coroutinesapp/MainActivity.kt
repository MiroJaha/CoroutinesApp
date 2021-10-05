package com.example.coroutinesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import org.json.JSONObject
import java.lang.Exception
import java.net.URL

class MainActivity : AppCompatActivity() {

    private lateinit var tView:TextView
    private lateinit var button: Button

    val adviceUrl = "https://api.adviceslip.com/advice"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tView = findViewById(R.id.tvMain)
        button = findViewById(R.id.button)

        var response = ""

        button.setOnClickListener {

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
                        val advice = slip.getString("advice")

                        tView.text = advice
                    }
                }
            }

        }
    }
}