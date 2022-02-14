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
    //default measurements displayed on app when opened
    var baseCurrency = "cup"
    var convertedToCurrency = "oz"
    var conversionRate = 0f
    var binding: ActivityConverterBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConverterBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        spinnerSetup()
    }

    //allows for the drop down measurement option in converter
    private fun spinnerSetup() {
        val spinner: Spinner = findViewById(R.id.spinner_firstConversion)
        val spinner2: Spinner = findViewById(R.id.spinner_secondConversion)

        ArrayAdapter.createFromResource(
            this,
            R.array.currencies, //array is stored in strings.xml
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

        //when selecting a measurement from the drop down spinner
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
                //the first one slected is the basecurrency, i.e. the measurement you want to convert
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
                //the second one selected is the measurement you want to convert too
                convertedToCurrency = parent?.getItemAtPosition(position).toString()
                read_json()
            } })
    }

    /*
        The convertion rates for all measurements can be found in
        assets -> converter.json
     */
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
                //read the json file
                val inputStream: InputStream = assets.open("converter.json")
                json = inputStream.bufferedReader().use { it.readText() }

                //convert to json object
                var jsonobj = JSONObject(json)

                //search for the basecurrency to find its available converter values
                var jsononj = jsonobj.getJSONObject(baseCurrency)
                Log.i("MAIN", "VALUE CONTENT IS: $jsononj")

                /*
                    amongst the values found above search for the convertion amount
                    to multiply with base currency
                 */
                conversionRate = jsononj.getString(convertedToCurrency).toFloat()
                Log.i("MAIN", "VALUE CONTENT INSIDE: $conversionRate")

                //once user clicks convert, solve for the amount
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


}

