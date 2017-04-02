package me.brereton.iching

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.ActionBarActivity
import android.view.Gravity
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import java.io.IOException
import java.util.ArrayList
import java.util.Date


class view_reading : ActionBarActivity() {

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent);
        finish();
    }

    fun transitional(hexagram: ArrayList<Int>?, date: Date?, question: String?) {


        // set the view to the finished hexagram
        setContentView(R.layout.activity_cast_hexagram)
        val view = findViewById(R.id.reading_view_id) as LinearLayout
        // create the scrollview that stores each bit of text
        title = "Reading"


        // create the text view
        val question_view = TextView(this)
        question_view.textSize = 20f
        var reading = ""
        reading = date.toString() + "\n\n" + question + "\n\n"
        question_view.text = reading
        // set the text view as the activity layout
        view.addView(question_view)


        // needed in order to deal with the changing lines one at a time
        val mutations = ArrayList<Int>()

        var first_hex_call = "h"
        var last_hex_call = "h"
        val base = get_base_hexagram(hexagram)
        val changed = get_changed_hexagram(hexagram)

        for (i in 0..5) {
            first_hex_call += base[i].toString()
            last_hex_call += changed[i].toString()
        }

        var prev_hex_call = first_hex_call

        // create the text view for the drawing of the first hexagram
        val first_hex_drawing_view = get_hexagram_ascii(hexagram)
        // set the text view as the activity layout
        view.addView(first_hex_drawing_view)

        // create the text view for the first hexagram
        val first_hex_view = TextView(this)
        first_hex_view.textSize = 20f
        first_hex_view.gravity = Gravity.CENTER
        val resId = this.resources.getIdentifier(first_hex_call, "string", this.packageName)
        val first_hex = "\n\n" + this.resources.getString(resId)
        first_hex_view.text = first_hex
        // set the text view as the activity layout
        view.addView(first_hex_view)



        for (i in 0..5) {
            val line = hexagram?.get(i) as Int
            if (line == 6 || line == 9) {
                mutations.add(i)
            }
        }



        if (mutations.size > 0) {

            // loop over number of mutations
            for (mut in mutations) {

                val changed_hexagram = ArrayList<Int>()

                for (j in 0..5) {
                    // if not yet at the mutation
                    if (j < mut) {
                        val line = hexagram?.get(j) as Int
                        if (line == 6) {
                            changed_hexagram.add(7)
                        } else if (line == 9) {
                            changed_hexagram.add(8)
                        } else {
                            changed_hexagram.add(line)
                        }
                    } else if (j == mut) {


                        val line = hexagram?.get(j) as Int
                        if (line == 6) {
                            changed_hexagram.add(7)
                        } else if (line == 9) {
                            changed_hexagram.add(8)
                        } else {
                            changed_hexagram.add(line)
                        }

                        val linecall = prev_hex_call + "l" + (mut + 1).toString()
                        val toss1ID = this.resources.getIdentifier(linecall, "string", this.packageName)
                        var lineText = this.resources.getString(toss1ID)

                        // create the text view for the moving line
                        val moving_lines_view = TextView(this)
                        moving_lines_view.textSize = 18f
                        lineText = "Changing Line:\n\n" + (mut + 1).toString() + ". " + lineText + "\n\n"
                        moving_lines_view.text = lineText
                        // set the text view as the activity layout
                        view.addView(moving_lines_view)


                    } else {
                        val line = hexagram?.get(j) as Int
                        changed_hexagram.add(line)
                    }


                }

                var hex_call = "h"
                val changed_base = get_base_hexagram(changed_hexagram)
                for (k in 0..5) {
                    hex_call += changed_base[k].toString()

                }



                if (prev_hex_call == hex_call) {
                } else {

                    // create the text view for the drawing of the first hexagram
                    val next_hex_drawing_view = get_hexagram_ascii(changed_hexagram)
                    // set the text view as the activity layout
                    view.addView(next_hex_drawing_view)

                    // create the text view for the second hexagram
                    val next_hex_view = TextView(this)
                    next_hex_view.textSize = 20f
                    next_hex_view.gravity = Gravity.CENTER
                    val resId_next = this.resources.getIdentifier(hex_call, "string", this.packageName)
                    val next_hex = "\n\n" + this.resources.getString(resId_next)
                    next_hex_view.text = next_hex
                    // set the text view as the activity layout
                    view.addView(next_hex_view)


                }

                prev_hex_call = hex_call


            }

        }

    }

    fun traditional(hexagram: ArrayList<Int>?, date: Date?, question: String?) {

        // set the view to the finished hexagram
        setContentView(R.layout.activity_cast_hexagram)
        val view = findViewById(R.id.reading_view_id) as LinearLayout
        // create the scrollview that stores each bit of text
        title = "Reading"

        // create the text view
        val question_view = TextView(this)
        question_view.textSize = 20f
        var reading = ""
        reading = date.toString() + "\n\n" + question + "\n\n"
        question_view.text = reading
        // set the text view as the activity layout
        view.addView(question_view)


        var first_hex_call = "h"
        var next_hex_call = "h"
        var moving_lines = ""

        val base = get_base_hexagram(hexagram)
        val changed = get_changed_hexagram(hexagram)

        for (i in 0..5) {
            first_hex_call = first_hex_call + base[i].toString()
            next_hex_call = next_hex_call + changed[i].toString()

        }


        // create the text view for the drawing of the first hexagram
        val first_hex_drawing_view = get_hexagram_ascii(hexagram)
        // set the text view as the activity layout
        view.addView(first_hex_drawing_view)

        // create the text view for the first hexagram
        val first_hex_view = TextView(this)
        first_hex_view.textSize = 20f
        first_hex_view.gravity = Gravity.CENTER
        val resId = this.resources.getIdentifier(first_hex_call, "string", this.packageName)
        val first_hex = "\n\n" + this.resources.getString(resId)
        first_hex_view.text = first_hex
        // set the text view as the activity layout
        view.addView(first_hex_view)


        // to get moving lines
        for (i in 0..5) {
            val line = hexagram?.get(i) as Int
            if (line == 6 || line == 9) {
                val linecall = first_hex_call + "l" + (i + 1).toString()
                val toss1ID = this.resources.getIdentifier(linecall, "string", this.packageName)
                val lineText = this.resources.getString(toss1ID)
                moving_lines = moving_lines + (i + 1).toString() + ". " + lineText + "\n\n"
            }
        }


        // only do this stuff if there are moving lines
        if (base == changed) {
        } else {

            // create the text view for the moving lines
            val moving_lines_view = TextView(this)
            moving_lines_view.textSize = 18f
            moving_lines = "Changing Lines:\n\n" + moving_lines
            moving_lines_view.text = moving_lines
            // set the text view as the activity layout
            view.addView(moving_lines_view)


            // create the text view for the drawing of the first hexagram
            val next_hex_drawing_view = get_hexagram_ascii(changed)
            // set the text view as the activity layout
            view.addView(next_hex_drawing_view)


            // create the text view for the second hexagram
            val next_hex_view = TextView(this)
            next_hex_view.textSize = 20f
            next_hex_view.gravity = Gravity.CENTER
            val resId_next = this.resources.getIdentifier(next_hex_call, "string", this.packageName)
            val next_hex = "\n\n" + this.resources.getString(resId_next)
            next_hex_view.text = next_hex
            // set the text view as the activity layout
            view.addView(next_hex_view)

        }


    }

    fun get_base_hexagram(hexagram: ArrayList<Int>?): ArrayList<Int> {


        val base_hexagram = ArrayList<Int>()

        for (i in 0..5) {

            val line = hexagram?.get(i) as Int

            if (line == 6) {
                base_hexagram.add(8)
            } else if (line == 9) {
                base_hexagram.add(7)
            } else {
                base_hexagram.add(line)
            }

        }

        return base_hexagram
    }

    fun get_hexagram_ascii(hexagram: ArrayList<Int>?): LinearLayout {

        val hex_drawing_view = LinearLayout(this)
        hex_drawing_view.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT)
        hex_drawing_view.orientation = LinearLayout.VERTICAL

        for (i in 5 downTo 0) {

            val line = hexagram?.get(i) as Int
            val res = resources

            if (line == 6) {
                val lineView = ImageView(this)
                val line_image = res.getDrawable(R.drawable.ic_line6)
                lineView.setImageDrawable(line_image)
                lineView.setPadding(0, 20, 0, 20)
                hex_drawing_view.addView(lineView)
            } else if (line == 9) {

                val lineView = ImageView(this)
                val line_image = res.getDrawable(R.drawable.ic_line9)
                lineView.setImageDrawable(line_image)
                lineView.setPadding(0, 20, 0, 20)
                hex_drawing_view.addView(lineView)

            } else if (line == 7) {

                val lineView = ImageView(this)
                val line_image = res.getDrawable(R.drawable.ic_line7)
                lineView.setImageDrawable(line_image)
                lineView.setPadding(0, 20, 0, 20)
                hex_drawing_view.addView(lineView)

            } else if (line == 8) {

                val lineView = ImageView(this)
                val line_image = res.getDrawable(R.drawable.ic_line8)
                lineView.setImageDrawable(line_image)
                lineView.setPadding(0, 20, 0, 20)
                hex_drawing_view.addView(lineView)

            }

        }

        return hex_drawing_view
    }

    fun get_changed_hexagram(hexagram: ArrayList<Int>?): ArrayList<Int> {


        val changed_hexagram = ArrayList<Int>()

        for (i in 0..5) {

            val line = hexagram?.get(i) as Int

            if (line == 6) {
                changed_hexagram.add(7)
            } else if (line == 9) {
                changed_hexagram.add(8)
            } else {
                changed_hexagram.add(line)
            }

        }

        return changed_hexagram
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val record_string = intent.getStringExtra("record")
        var record: MainActivity.ReadingRecord? = null
        try {
            record = MainActivity.fromString(record_string) as MainActivity.ReadingRecord
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }


        val date = record?.date
        val question = record?.question
        val hexagram = record?.hexagram


        val prefs = this.getSharedPreferences("me.brereton.iching", Context.MODE_PRIVATE)
        val transitional_check = prefs.getBoolean("transitional_check", false)


        if (transitional_check) {
            transitional(hexagram, date, question)
        } else {
            traditional(hexagram, date, question)
        }


    }


}