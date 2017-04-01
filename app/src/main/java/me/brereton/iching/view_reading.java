package me.brereton.iching;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;



public class view_reading extends ActionBarActivity {


    public void transitional(ArrayList hexagram, Date date, String question) {


        // set the view to the finished hexagram
        setContentView(R.layout.activity_cast_hexagram);
        LinearLayout view = (LinearLayout) findViewById(R.id.reading_view_id);
        // create the scrollview that stores each bit of text
        setTitle("Reading");



        // create the text view
        TextView question_view = new TextView(this);
        question_view.setTextSize(20);
        String reading = "";
        reading = (String.valueOf(date) + "\n\n" + question + "\n\n");
        question_view.setText(reading);
        // set the text view as the activity layout
        view.addView(question_view);



        // needed in order to deal with the changing lines one at a time
        ArrayList<Integer> mutations = new ArrayList<>();

        String first_hex_call = "h";
        String last_hex_call = "h";
        ArrayList base = get_base_hexagram(hexagram);
        ArrayList changed = get_changed_hexagram(hexagram);

        for(int i=0;i<6;i++) {
            first_hex_call = first_hex_call + String.valueOf(base.get(i));
            last_hex_call = last_hex_call + String.valueOf(changed.get(i));

        }

        String prev_hex_call = first_hex_call;

        // create the text view for the drawing of the first hexagram
        LinearLayout first_hex_drawing_view = get_hexagram_ascii(hexagram);
        // set the text view as the activity layout
        view.addView(first_hex_drawing_view);

        // create the text view for the first hexagram
        TextView first_hex_view = new TextView(this);
        first_hex_view.setTextSize(20);
        first_hex_view.setGravity(Gravity.CENTER);
        int resId = this.getResources().getIdentifier(first_hex_call, "string", this.getPackageName());
        String first_hex = "\n\n" + this.getResources().getString(resId);
        first_hex_view.setText(first_hex);
        // set the text view as the activity layout
        view.addView(first_hex_view);



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

                        // create the text view for the moving line
                        TextView moving_lines_view = new TextView(this);
                        moving_lines_view.setTextSize(18);
                        lineText = "Changing Line:\n\n" + String.valueOf(mutations.get(i) + 1) + ". " + lineText + "\n\n";
                        moving_lines_view.setText(lineText);
                        // set the text view as the activity layout
                        view.addView(moving_lines_view);



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

                    // create the text view for the drawing of the first hexagram
                    LinearLayout next_hex_drawing_view = get_hexagram_ascii(changed_hexagram);
                    // set the text view as the activity layout
                    view.addView(next_hex_drawing_view);

                    // create the text view for the second hexagram
                    TextView next_hex_view = new TextView(this);
                    next_hex_view.setTextSize(20);
                    next_hex_view.setGravity(Gravity.CENTER);
                    int resId_next = this.getResources().getIdentifier(hex_call, "string", this.getPackageName());
                    String next_hex = "\n\n" + this.getResources().getString(resId_next);
                    next_hex_view.setText(next_hex);
                    // set the text view as the activity layout
                    view.addView(next_hex_view);



                }

                prev_hex_call = hex_call;



            }

        }

    };

    public void traditional(ArrayList hexagram, Date date, String question) {

        // set the view to the finished hexagram
        setContentView(R.layout.activity_cast_hexagram);
        LinearLayout view = (LinearLayout) findViewById(R.id.reading_view_id);
        // create the scrollview that stores each bit of text
        setTitle("Reading");

        // create the text view
        TextView question_view = new TextView(this);
        question_view.setTextSize(20);
        String reading = "";
        reading = (String.valueOf(date) + "\n\n" + question + "\n\n");
        question_view.setText(reading);
        // set the text view as the activity layout
        view.addView(question_view);


        String first_hex_call = "h";
        String next_hex_call = "h";
        String moving_lines = "";

        ArrayList base = get_base_hexagram(hexagram);
        ArrayList changed = get_changed_hexagram(hexagram);

        for(int i=0;i<6;i++) {
            first_hex_call = first_hex_call + String.valueOf(base.get(i));
            next_hex_call = next_hex_call + String.valueOf(changed.get(i));

        }


        // create the text view for the drawing of the first hexagram
        LinearLayout first_hex_drawing_view = get_hexagram_ascii(hexagram);
        // set the text view as the activity layout
        view.addView(first_hex_drawing_view);

        // create the text view for the first hexagram
        TextView first_hex_view = new TextView(this);
        first_hex_view.setTextSize(20);
        first_hex_view.setGravity(Gravity.CENTER);
        int resId = this.getResources().getIdentifier(first_hex_call, "string", this.getPackageName());
        String first_hex = "\n\n" + this.getResources().getString(resId);
        first_hex_view.setText(first_hex);
        // set the text view as the activity layout
        view.addView(first_hex_view);



        // to get moving lines
        for(int i=0;i<6;i++) {
            int line = (int) hexagram.get(i);
            if (line == 6 || line == 9) {
                String linecall = first_hex_call + "l" + String.valueOf(i + 1);
                int toss1ID = this.getResources().getIdentifier(linecall, "string", this.getPackageName());
                String lineText = this.getResources().getString(toss1ID);
                moving_lines = moving_lines + String.valueOf(i + 1) + ". " + lineText + "\n\n";
            }
        }


        // only do this stuff if there are moving lines
        if (base.equals(changed)) {} else {

            // create the text view for the moving lines
            TextView moving_lines_view = new TextView(this);
            moving_lines_view.setTextSize(18);
            moving_lines = "Changing Lines:\n\n" + moving_lines;
            moving_lines_view.setText(moving_lines);
            // set the text view as the activity layout
            view.addView(moving_lines_view);


            // create the text view for the drawing of the first hexagram
            LinearLayout next_hex_drawing_view = get_hexagram_ascii(changed);
            // set the text view as the activity layout
            view.addView(next_hex_drawing_view);


            // create the text view for the second hexagram
            TextView next_hex_view = new TextView(this);
            next_hex_view.setTextSize(20);
            next_hex_view.setGravity(Gravity.CENTER);
            int resId_next = this.getResources().getIdentifier(next_hex_call, "string", this.getPackageName());
            String next_hex = "\n\n" + this.getResources().getString(resId_next);
            next_hex_view.setText(next_hex);
            // set the text view as the activity layout
            view.addView(next_hex_view);

        }



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

    public LinearLayout get_hexagram_ascii(ArrayList hexagram) {

        LinearLayout hex_drawing_view = new LinearLayout(this);
        hex_drawing_view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        hex_drawing_view.setOrientation(LinearLayout.VERTICAL);

        for(int i=5;i>=0;i--) {

            int line = (int) hexagram.get(i);
            Resources res = getResources();

            if (line == 6) {
                ImageView lineView = new ImageView(this);
                Drawable line_image = res.getDrawable(R.drawable.ic_line6);
                lineView.setImageDrawable(line_image);
                lineView.setPadding(0,20,0,20);
                hex_drawing_view.addView(lineView);
            } else if (line == 9) {

                ImageView lineView = new ImageView(this);
                Drawable line_image = res.getDrawable(R.drawable.ic_line9);
                lineView.setImageDrawable(line_image);
                lineView.setPadding(0,20,0,20);
                hex_drawing_view.addView(lineView);

            } else if (line == 7) {

                ImageView lineView = new ImageView(this);
                Drawable line_image = res.getDrawable(R.drawable.ic_line7);
                lineView.setImageDrawable(line_image);
                lineView.setPadding(0,20,0,20);
                hex_drawing_view.addView(lineView);

            } else if (line == 8) {

                ImageView lineView = new ImageView(this);
                Drawable line_image = res.getDrawable(R.drawable.ic_line8);
                lineView.setImageDrawable(line_image);
                lineView.setPadding(0,20,0,20);
                hex_drawing_view.addView(lineView);

            }

        }

        return hex_drawing_view;
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

        String record_string = getIntent().getStringExtra("record");
        MainActivity.ReadingRecord record = null;
        try {
            record = (MainActivity.ReadingRecord) MainActivity.fromString(record_string);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


        Date date = record.date;
        String question = record.question;
        ArrayList<Integer> hexagram = record.hexagram;


        SharedPreferences prefs = this.getSharedPreferences("me.brereton.iching", Context.MODE_PRIVATE);
        boolean transitional_check = prefs.getBoolean("transitional_check", false);


        if (transitional_check) {
            transitional(hexagram, date, question);
        } else {
            traditional(hexagram, date, question);
        };


    }


}