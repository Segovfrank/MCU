package com.clase.myapplication

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

class MCUMarvelVolley(
    private val url: String,
    private val context: Context,
    private val adapter: AdapterMCU
) {

    private val queue = Volley.newRequestQueue(context)

    fun generateRequest() {
        val dataHeroes = mutableListOf<Hero>()

        val request = JsonObjectRequest(
            Request.Method.GET,
            url,
            null,
            Response.Listener {
                val heroes = it.getJSONObject("data").getJSONArray("results")
                for (i in 0 until heroes.length()) {
                    val jObject = heroes.getJSONObject(i)
                    var name = try {
                        jObject["name"] as String
                    } catch (ex: java.lang.Exception) {
                        Log.e("JSON", "Error ${ex.message}")
                        null
                    }
                    var notes = try {
                        jObject["description"] as String
                    } catch (ex: java.lang.Exception) {
                        Log.e("JSON", "Error ${ex.message}")
                        null
                    }

                    var thumbnail = try {
                        jObject["thumbnail"] as String
                    } catch (ex: java.lang.Exception) {
                        Log.e("JSON", "Error ${ex.message}")
                        null
                    }
                    dataHeroes.add(Hero(name ?: "Undefined", notes, thumbnail))

                }
                adapter.setData(dataHeroes)
            },
            Response.ErrorListener {
                Log.e("API_REQUEST", "Error ${it.networkResponse}")
            }
        )

        queue.add(request)
    }

}