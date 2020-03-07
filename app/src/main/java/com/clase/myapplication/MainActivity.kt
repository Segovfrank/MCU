package com.clase.myapplication

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONArray
import java.math.BigInteger
import java.security.MessageDigest
import java.sql.Timestamp

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AdapterMCU

    companion object{
        private const val PUBLIC_KEY = "409bfd5e7c63c8b4f5dbd742ebeece56"
        private const val PRIVATE_KEY = "299d2b414c032a94ac3176fd506dd8da15adc288"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
    }

    private fun initViews() {
        //val heroes = getDataset()

        recyclerView = findViewById(R.id.recycler)
        adapter = AdapterMCU()
        recyclerView.adapter = adapter
        val apiURL = getMarvelAPIUrl()
        Log.d("API_URL", "URL: $apiURL")
        MCUMarvelVolley(apiURL, this, adapter).generateRequest()
    }

    private fun getMarvelAPIUrl(): String {
        val tString = Timestamp(System.currentTimeMillis()).toString()
        Log.d("API", "Tmp: $tString")
        val hString = tString + PRIVATE_KEY + PUBLIC_KEY

        val hash = hString.md5()

        return "https://gateway.marvel.com:443/v1/public/characters?ts=$tString&limit=100&apikey=$PUBLIC_KEY&hash=$hash"
    }

    private fun String.md5(): String{
        return BigInteger(MessageDigest.getInstance("MD5")
            .digest(toByteArray())).toString(16)
            .padStart(32, '0')
    }

    private fun readJSON(): String? {
        var jContent: String? = null
        try {
            val inputStream = assets.open("avengers.json")
            jContent = inputStream.bufferedReader().use { it.readLine() }

        } catch (ex: Exception) {
            Log.e("JSONr", "Error: ${ex.message}")
            Toast.makeText(this, "Error con el JSON", Toast.LENGTH_LONG).show()
            null
        }
        return jContent
    }

    private fun getDataset(): List<Hero> {
        val res = readJSON()
        //Log.d("JSON", "RES = $res")
        val jArray = JSONArray(res)
        val heroes = mutableListOf<Hero>()

        for (i in 0 until jArray.length()) {
            //Log.d("JSON", "Alias = ${jArray.getJSONObject(i)["name/alias"]}")
            val jObject = jArray.getJSONObject(i)
            var name = try {
                jObject["name/alias"] as String
            } catch (ex: java.lang.Exception) {
                Log.e("JSON", "Error ${ex.message}")
                null
            }
            var notes = try {
                jObject["notes"] as String
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


            heroes.add(Hero(name ?: "Undefined", notes, thumbnail))

        }

        return heroes
    }
}
