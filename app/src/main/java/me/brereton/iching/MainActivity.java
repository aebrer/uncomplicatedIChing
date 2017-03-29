package me.brereton.iching;
        import android.content.Context;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.support.v7.app.ActionBarActivity;
        import android.os.Bundle;
        import android.util.Base64;
        import android.util.Log;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;
        import android.widget.CheckBox;
        import android.widget.EditText;
        import android.widget.ListView;
        import android.widget.Toast;

        import java.io.ByteArrayInputStream;
        import java.io.ByteArrayOutputStream;
        import java.io.IOException;
        import java.io.ObjectInputStream;
        import java.io.ObjectOutputStream;
        import java.io.Serializable;
        import java.util.ArrayList;
        import java.util.Collections;
        import java.util.Date;


public class MainActivity extends ActionBarActivity {

    public final static String EXTRA_MESSAGE = "me.brereton.iching.MESSAGE";



    /** Read the object from Base64 string. */
    public static Object fromString( String s ) throws IOException ,
            ClassNotFoundException {
        byte [] data = Base64.decode(s, Base64.DEFAULT);
        ObjectInputStream ois = new ObjectInputStream(
                new ByteArrayInputStream(  data ) );
        Object o  = ois.readObject();
        ois.close();
        return o;
    }

    /** Write the object to a Base64 string. */
    public static String toString( Serializable o ) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream( baos );
        oos.writeObject( o );
        oos.close();
        return Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
    }



    public static class ReadingRecord implements java.io.Serializable{
        public String question;
        public Date date;
        public ArrayList<Integer> hexagram;


        public ReadingRecord(String question, Date date, ArrayList<Integer> hexagram) {
            this.question = question;
            this.date = date;
            this.hexagram = hexagram;

        }

        @Override
        public String toString() {
            return this.date + ": " + this.question;
        }


    }

    public void sendMessage(View view) {
        // Do something in response to the button

        Intent intent = new Intent(this, cast_hexagram.class);
        EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    public void viewHistory(View view) {
        // Do something in response to the button

        Intent intent = new Intent(this, view_history.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }





    @Override
    public void onPause() {
        CheckBox checkBox1 = (CheckBox) findViewById(R.id.transitional);
        super.onPause();
        save(checkBox1.isChecked());
    }

    @Override
    public void onResume() {
        CheckBox checkBox1 = (CheckBox) findViewById(R.id.transitional);
        super.onResume();
        checkBox1.setChecked(load());



        // get the record list
        ArrayList<ReadingRecord> history_array = new ArrayList<ReadingRecord>();

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


        final ListView lv = (ListView) findViewById(R.id.history_list);

        //reverse the order of the list
        Collections.reverse(history_array);


        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, android.R.id.text1, history_array);



        // Assign adapter to ListView
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {

                // ListView Clicked item value
                ReadingRecord record = (ReadingRecord) lv.getItemAtPosition(position);

                String record_string = "";

                try {
                    record_string = MainActivity.toString(record);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent(getApplicationContext(), view_history.class);
                intent.putExtra("record", record_string);
                startActivity(intent);




            }
        });





    }

    private void save(final boolean isChecked) {
        SharedPreferences sharedPreferences = getSharedPreferences("me.brereton.iching", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("transitional_check", isChecked);
        editor.commit();
    }

    private boolean load() {
        SharedPreferences sharedPreferences = getSharedPreferences("me.brereton.iching", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("transitional_check", false);
    }

}