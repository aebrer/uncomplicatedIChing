package me.brereton.iching;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.RequiresPermission;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Base64;
import android.util.Log;
import android.widget.ScrollView;
import android.widget.TextView;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.StringBufferInputStream;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Date;



public class cast_hexagram extends ActionBarActivity {


    // init the RNG
    public SecureRandom generator = new SecureRandom();

    public int get_toss() {

        // calculate hexagrams
        // 6 -> 1/16, 7 -> 5/16, 8 -> 7/16, 9 -> 3/16
        int sample = generator.nextInt(16);
        int toss = 0;

        if (sample == 0) {toss = 6;}
        if (sample > 0 && sample <= 5) {toss = 7;}
        if (sample >= 6 && sample <= 12) {toss = 8;}
        if (sample > 12) {toss = 9;}

        return toss;

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Date date = new Date();

        Log.d("date", String.valueOf(date));

        // get the message from the intent
        Intent intent = getIntent();
        String question = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        // need to store these as one object, called the hexagram

        ArrayList<Integer> hexagram = new ArrayList<>();

        for(int i=0;i<6;i++) {
            int toss = get_toss();
            hexagram.add(toss);
        }



        // should now store the date, question, and hexagram in the persistent memory
        // hexagram, date, question


        // get the record list from the memory, in order to add the new reading to it
        ArrayList<MainActivity.ReadingRecord> history_array = new ArrayList<MainActivity.ReadingRecord>();

        SharedPreferences sharedPreferences = getSharedPreferences("me.brereton.iching", Context.MODE_PRIVATE);
        String history_array_string = sharedPreferences.getString("history", "");

        Log.d("debugstring", history_array_string);
        Object debug = null;
        try {
            debug = MainActivity.fromString(history_array_string);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        // need this dummy hexagram business so that the main view won't crash if there is no history yet
        if (debug != null) {

            try {
                history_array = (ArrayList<MainActivity.ReadingRecord>) MainActivity.fromString(history_array_string);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassCastException e) {
                ArrayList<Integer> dummy_hexagram = new ArrayList<>();
                dummy_hexagram.add(6);
                dummy_hexagram.add(6);
                dummy_hexagram.add(6);
                dummy_hexagram.add(6);
                dummy_hexagram.add(6);
                dummy_hexagram.add(6);
                Date dummy_date = new Date();
                MainActivity.ReadingRecord dummy_record = new MainActivity.ReadingRecord("dummy_record", dummy_date, dummy_hexagram);
                history_array.add(dummy_record);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }


        } else {

            ArrayList<Integer> dummy_hexagram = new ArrayList<>();
            dummy_hexagram.add(6);
            dummy_hexagram.add(6);
            dummy_hexagram.add(6);
            dummy_hexagram.add(6);
            dummy_hexagram.add(6);
            dummy_hexagram.add(6);
            Date dummy_date = new Date();
            MainActivity.ReadingRecord dummy_record = new MainActivity.ReadingRecord("dummy_record", dummy_date, dummy_hexagram);
            history_array.add(dummy_record);

        }





        MainActivity.ReadingRecord this_reading = new MainActivity.ReadingRecord(question, date, hexagram);
        // add reading record to array of records
        history_array.add(this_reading);

        try {
            history_array_string = MainActivity.toString(history_array);
        } catch (IOException e) {
            e.printStackTrace();
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("history", history_array_string);
        editor.commit();



        // Now that reading is added to the history, call the view reading method.
        if (this_reading.question.equals("dummy_record")) {} else{
            String record_string = "";

            try {
                record_string = MainActivity.toString(this_reading);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Intent view_intent = new Intent(getApplicationContext(), view_reading.class);
            view_intent.putExtra("record", record_string);
            startActivity(view_intent);

        }



    }

}

