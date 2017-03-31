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

    public void transitional(ArrayList hexagram,  Date date, String question) {



        ArrayList<Integer> mutations = new ArrayList<>();
        String reading = "";

        String first_hex_call = "h";
        String last_hex_call = "h";
        ArrayList base = get_base_hexagram(hexagram);
        ArrayList changed = get_changed_hexagram(hexagram);

        for(int i=0;i<6;i++) {
            first_hex_call = first_hex_call + String.valueOf(base.get(i));
            last_hex_call = last_hex_call + String.valueOf(changed.get(i));

        }

        String prev_hex_call = first_hex_call;

        int resId = this.getResources().getIdentifier(first_hex_call, "string", this.getPackageName());
        String first_hex = get_hexagram_ascii(hexagram) + "\n" + this.getResources().getString(resId);
        reading = String.valueOf(date) + "\n\n" + question + "\n\n\n" + first_hex;

        for(int i=0;i<6;i++) {
            int line = (int) hexagram.get(i);
            if (line == 6 || line == 9) {
                mutations.add(i);
            }
        }



        if (mutations.size() > 0) {


            // loop over number of mutations
            for(int i=0;i<mutations.size();i++) {

                ArrayList<Integer> changed_hexagram = new ArrayList<>();

                for (int j=0;j<6;j++) {
                    // if not yet at the mutation
                    if (j < mutations.get(i)) {
                        int line = (int) hexagram.get(j);
                        if (line == 6) {
                            changed_hexagram.add(7);
                        } else if (line == 9) {
                            changed_hexagram.add(8);
                        } else {
                            changed_hexagram.add(line);
                        }
                    } else if (j == mutations.get(i)) {


                        int line = (int) hexagram.get(j);
                        if (line == 6) {
                            changed_hexagram.add(7);
                        } else if (line == 9) {
                            changed_hexagram.add(8);
                        } else {
                            changed_hexagram.add(line);
                        }

                        String linecall = prev_hex_call + "l" + String.valueOf(mutations.get(i) + 1);
                        int toss1ID = this.getResources().getIdentifier(linecall, "string", this.getPackageName());
                        String lineText = this.getResources().getString(toss1ID);
                        reading = reading + "\n\n Changing Line:\n" + String.valueOf(mutations.get(i) + 1) + ". " + lineText;


                    } else {
                        int line = (int) hexagram.get(j);
                        changed_hexagram.add(line);
                    }



                }

                String hex_call = "h";
                ArrayList changed_base = get_base_hexagram(changed_hexagram);
                for(int k=0;k<6;k++) {
                    hex_call = hex_call + String.valueOf(changed_base.get(k));

                }



                if (prev_hex_call.equals(hex_call)) {
                } else {
                    resId = this.getResources().getIdentifier(hex_call, "string", this.getPackageName());
                    String hex = get_hexagram_ascii(changed_hexagram) + "\n" + this.getResources().getString(resId);
                    reading = reading + "\n\n\n\n" +  hex;
                }

                prev_hex_call = hex_call;



            }

        }





        // create the text view
        TextView textView = new TextView(this);
        textView.setTextSize(15);
        textView.setText(reading);
        textView.setMovementMethod(new ScrollingMovementMethod());


        // set the text view as the activity layout
        setTitle("Reading");
        ScrollView scrollView = new ScrollView(this);
        scrollView.addView(textView);
        setContentView(scrollView);


    };

    public void traditional(ArrayList hexagram, Date date, String question) {

        String first_hex_call = "h";
        String next_hex_call = "h";
        String moving_lines = "";
        ArrayList base = get_base_hexagram(hexagram);
        ArrayList changed = get_changed_hexagram(hexagram);

        for(int i=0;i<6;i++) {
            first_hex_call = first_hex_call + String.valueOf(base.get(i));
            next_hex_call = next_hex_call + String.valueOf(changed.get(i));

        }

        // loop again to get moving lines
        for(int i=0;i<6;i++) {
            int line = (int) hexagram.get(i);
            if (line == 6 || line == 9) {
                String linecall = first_hex_call + "l" + String.valueOf(i + 1);
                int toss1ID = this.getResources().getIdentifier(linecall, "string", this.getPackageName());
                String lineText = this.getResources().getString(toss1ID);
                moving_lines = moving_lines + "\n\n" + String.valueOf(i + 1) + ". " + lineText;
            }
        }



        int resId = this.getResources().getIdentifier(first_hex_call, "string", this.getPackageName());
        String first_hex = get_hexagram_ascii(hexagram) + "\n" + this.getResources().getString(resId);
        int resId_next = this.getResources().getIdentifier(next_hex_call, "string", this.getPackageName());
        String next_hex = get_hexagram_ascii(changed) + "\n" + this.getResources().getString(resId_next);


        // create the text view
        TextView textView = new TextView(this);
        textView.setTextSize(15);
        String reading = "";
        if (first_hex.equals(next_hex)) {
            reading = (String.valueOf(date) + "\n\n" + question + "\n\n\n" + first_hex + "\n\n");
        } else {
            reading = (String.valueOf(date) + "\n\n" + question + "\n\n\n" + first_hex + "\n\nChanging lines:" + moving_lines + "\n\n\n\n" + next_hex);
        };
        textView.setText(reading);
        textView.setMovementMethod(new ScrollingMovementMethod());


        // set the text view as the activity layout
        setTitle("Reading");
        ScrollView scrollView = new ScrollView(this);
        scrollView.addView(textView);
        setContentView(scrollView);





    };

    public ArrayList get_base_hexagram(ArrayList hexagram) {


        ArrayList<Integer> base_hexagram = new ArrayList<>();

        for(int i=0;i<6;i++) {

            int line = (int) hexagram.get(i);

            if (line == 6) {
                base_hexagram.add(8);
            } else if (line == 9) {
                base_hexagram.add(7);
            } else {
                base_hexagram.add(line);
            }

        }

        return base_hexagram;
    };

    public String get_hexagram_ascii(ArrayList hexagram) {

        String hexImage = "";

        for(int i=5;i>=0;i--) {

            int line = (int) hexagram.get(i);

            if (line == 6) {
                hexImage = hexImage + "  --- X ---\n";
            } else if (line == 9) {
                hexImage = hexImage + "  ----o----\n";
            } else if (line == 7) {
                hexImage = hexImage + "  ---------\n";
            } else if (line == 8) {
                hexImage = hexImage + "  ---   ---\n";
            }

        }

        return hexImage;
    };

    public ArrayList get_changed_hexagram(ArrayList hexagram) {


        ArrayList<Integer> changed_hexagram = new ArrayList<>();

        for(int i=0;i<6;i++) {

            int line = (int) hexagram.get(i);

            if (line == 6) {
                changed_hexagram.add(7);
            } else if (line == 9) {
                changed_hexagram.add(8);
            } else {
                changed_hexagram.add(line);
            }

        }

        return changed_hexagram;
    };


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

        SharedPreferences prefs = this.getSharedPreferences("me.brereton.iching", Context.MODE_PRIVATE);
        boolean transitional_check = prefs.getBoolean("transitional_check", false);


        Log.d("transitional", String.valueOf(transitional_check));


        if (transitional_check) {
            transitional(hexagram, date, question);
        } else {
            traditional(hexagram, date, question);
        };






        // get the record list
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



        Log.d("stringified2", String.valueOf(history_array.get(history_array.size() - 1).question));


        // should now store the date, question, and hexagram in the persistent memory
        // hexagram, date, question

        MainActivity.ReadingRecord this_reading = new MainActivity.ReadingRecord(question, date, hexagram);
        Log.d("stringified2 this_read", String.valueOf(this_reading.question));

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


    }

}

