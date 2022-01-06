package com.mobdeve.s12.avila.tiu.littlechef

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import com.mobdeve.s12.avila.tiu.littlechef.databinding.ActivityConverterBinding
import kotlinx.android.synthetic.main.activity_converter.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.InputStream
import java.lang.Exception
import java.net.URL

class ConverterActivity : DrawerBaseActivity() {
    var baseCurrency = "cup"
    var convertedToCurrency = "oz"
    var conversionRate = 0f
    var binding: ActivityConverterBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConverterBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        spinnerSetup()
        textChangedStuff()
    }
    fun read_json(){
        var json: String? = null
        try {
            if (baseCurrency == convertedToCurrency) {
                Toast.makeText(
                    applicationContext,
                    "Please pick a currency to convert",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val inputStream: InputStream = assets.open("converter.json")
                json = inputStream.bufferedReader().use { it.readText() }

                var jsonobj = JSONObject(json)
                var jsononj = jsonobj.getJSONObject(baseCurrency)
                Log.i("MAIN", "VALUE CONTENT IS: $jsononj")

                conversionRate = jsononj.getString(convertedToCurrency).toFloat()
                Log.i("MAIN", "VALUE CONTENT INSIDE: $conversionRate")

                binding!!.convert.setOnClickListener {
                    val text =
                        ((et_firstConversion.text.toString()
                            .toFloat()) * conversionRate).toString()
                    et_secondConversion?.setText(text)
                }
            }
        }catch (e:Exception){
            Log.d("Main", "exception error $e")
        }

    }

    private fun spinnerSetup() {
        val spinner: Spinner = findViewById(R.id.spinner_firstConversion)
        val spinner2: Spinner = findViewById(R.id.spinner_secondConversion)

        ArrayAdapter.createFromResource(
            this,
            R.array.currencies,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        ArrayAdapter.createFromResource(
            this,
            R.array.currencies2,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner2.adapter = adapter
        }

        spinner.onItemSelectedListener = (object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                baseCurrency = parent?.getItemAtPosition(position).toString()
                read_json()
            } })

        spinner2.onItemSelectedListener = (object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                convertedToCurrency = parent?.getItemAtPosition(position).toString()
                read_json()
            } })
    }

    private fun textChangedStuff() {
        et_firstConversion.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                try {
                    read_json()
                } catch (e: Exception) {
                    Toast.makeText(applicationContext, "Type a value", Toast.LENGTH_SHORT).show()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                Log.d("Main", "Before Text Changed")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Log.d("Main", "OnTextChanged")
            }

        })

    }


}

