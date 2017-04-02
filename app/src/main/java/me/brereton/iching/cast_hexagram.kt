package me.brereton.iching


import android.content.Context
import android.content.Intent
import android.support.v7.app.ActionBarActivity
import android.os.Bundle
import java.io.IOException
import java.security.SecureRandom
import java.util.ArrayList
import java.util.Date


class cast_hexagram : ActionBarActivity() {


    // init the RNG
    var generator = SecureRandom()

    // calculate hexagrams
    // 6 -> 1/16, 7 -> 5/16, 8 -> 7/16, 9 -> 3/16
    val _toss: Int
        get() {
            val sample = generator.nextInt(16)
            var toss = 0

            if (sample == 0) {
                toss = 6
            }
            if (sample > 0 && sample <= 5) {
                toss = 7
            }
            if (sample >= 6 && sample <= 12) {
                toss = 8
            }
            if (sample > 12) {
                toss = 9
            }

            return toss

        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val date = Date()


        // get the message from the intent
        val intent = intent
        val question = intent.getStringExtra(MainActivity.EXTRA_MESSAGE)

        // need to store these as one object, called the hexagram

        val hexagram = ArrayList<Int>()

        for (i in 0..5) {
            val toss = _toss
            hexagram.add(toss)
        }


        // should now store the date, question, and hexagram in the persistent memory
        // hexagram, date, question


        // get the record list from the memory, in order to add the new reading to it
        var history_array = ArrayList<MainActivity.ReadingRecord>()

        val sharedPreferences = getSharedPreferences("me.brereton.iching", Context.MODE_PRIVATE)
        var history_array_string: String = sharedPreferences.getString("history", "")


        try {
            history_array = MainActivity.fromString(history_array_string) as ArrayList<MainActivity.ReadingRecord>
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: ClassCastException) {
            e.printStackTrace()
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }



        val this_reading = MainActivity.ReadingRecord(question, date, hexagram)
        // add reading record to array of records
        history_array.add(this_reading)

        try {
            history_array_string = MainActivity.toString(history_array)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        val editor = sharedPreferences.edit()
        editor.putString("history", history_array_string)
        editor.commit()


        // Now that reading is added to the history, call the view reading method.

        var record_string = ""

        try {
            record_string = MainActivity.toString(this_reading)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        val view_intent = Intent(applicationContext, view_reading::class.java)
        view_intent.putExtra("record", record_string)
        startActivity(view_intent)




    }

}

