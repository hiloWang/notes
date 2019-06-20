package me.ztiany.jsonanalyze

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.alibaba.fastjson.JSONObject
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import me.ztiany.jsonanalyze.address.AddressInquirers
import me.ztiany.jsonanalyze.kotlin.PERSON_JSON
import me.ztiany.jsonanalyze.kotlin.Person
import me.ztiany.jsonanalyze.model.ModelParser

class MainActivity : AppCompatActivity() {

    private val addressInquirers = AddressInquirers()
    private val bigDataModuleParser = ModelParser(true)
    private val normalDataModuleParser = ModelParser(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun parseAddressByGson(view: View) {
        val time = addressInquirers.start(AddressInquirers.PARSER_TYPE_GSON)
        parseTimeGson.text = time.toString()
    }

    fun parseAddressByFastJson(view: View) {
        val time = addressInquirers.start(AddressInquirers.PARSER_TYPE_FASTJSON)
        parseTimeFastJson.text = time.toString()
    }

    fun parseBigDataAddressByGson(view: View) {
        val time = bigDataModuleParser.start(ModelParser.PARSER_TYPE_GSON)
        bigDataParseTimeGson.text = time.toString()
    }

    fun parseBigDataAddressByFastJson(view: View) {
        val time = bigDataModuleParser.start(ModelParser.PARSER_TYPE_FASTJSON)
        bigDataParseTimeFastJson.text = time.toString()
    }

    fun parseNormalDataAddressByGson(view: View) {
        val time = normalDataModuleParser.start(ModelParser.PARSER_TYPE_GSON)
        normalDataParseTimeGson.text = time.toString()
    }

    fun parseNormalDataAddressByFastJson(view: View) {
        val time = normalDataModuleParser.start(ModelParser.PARSER_TYPE_FASTJSON)
        normalDataParseTimeFastJson.text = time.toString()
    }

    fun parseKotlinByGson(view: View) {
        try {
            val parseObject = Gson().fromJson<Person>(PERSON_JSON, Person::class.java)
            Toast.makeText(this, parseObject.toString(), Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(this, "parse error: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    fun parseKotlinByFastJson(view: View) {
        try {
            val parseObject = JSONObject.parseObject(PERSON_JSON, Person::class.java)
            Toast.makeText(this, parseObject.toString(), Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(this, "parse error: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

}
