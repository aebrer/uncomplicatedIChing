package me.brereton.iching

import android.content.Context
import android.content.Intent
import android.support.v7.app.ActionBarActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.text.InputType
import android.util.Base64
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ListView
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable
import java.util.ArrayList
import java.util.Collections
import java.util.Date


class MainActivity : ActionBarActivity() {

    override fun onBackPressed() {
        val startMain = Intent(Intent.ACTION_MAIN)
        startMain.addCategory(Intent.CATEGORY_HOME)
        startMain.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(startMain)
    }

    class ReadingRecord(// this "record" is the object that stores the details required for a given reading
            var question: String, var date: Date, var hexagram: ArrayList<Int>) : java.io.Serializable {

        // this is to show the right text in the listview being displayed on the main screen
        override fun toString(): String {
            return this.date.toString() + ":\n" + this.question
        }


    }

    fun sendMessage(view: View) {
        // Do something in response to the button

        val intent = Intent(this, cast_hexagram::class.java)
        val editText = findViewById(R.id.edit_message) as EditText
        editText.setInputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_SENTENCES)
        val message = editText.text.toString()
        intent.putExtra(EXTRA_MESSAGE, message)
        startActivity(intent)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId
        if (id == R.id.action_settings) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }


    public override fun onPause() {
        val checkBox1 = findViewById(R.id.transitional) as CheckBox
        super.onPause()
        save(checkBox1.isChecked)
    }

    public override fun onResume() {
        val checkBox1 = findViewById(R.id.transitional) as CheckBox
        super.onResume()
        checkBox1.isChecked = load()


        // get the record list
        var history_array = ArrayList<ReadingRecord>()

        val sharedPreferences = getSharedPreferences("me.brereton.iching", Context.MODE_PRIVATE)
        val history_array_string = arrayOf(sharedPreferences.getString("history", ""))


        try {
            history_array = MainActivity.fromString(history_array_string[0]) as ArrayList<MainActivity.ReadingRecord>
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: ClassCastException) {
            e.printStackTrace()
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }


        val lv = findViewById(R.id.history_list) as ListView

        //reverse the order of the list
        Collections.reverse(history_array)

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, android.R.id.text1, history_array)


        // Assign adapter to ListView
        lv.adapter = adapter

        lv.onItemClickListener = AdapterView.OnItemClickListener { arg0, arg1, position, arg3 ->
            // ListView Clicked item value
            val record = lv.getItemAtPosition(position) as ReadingRecord
            var record_string = ""

            try {
                record_string = MainActivity.toString(record)
            } catch (e: IOException) {
                e.printStackTrace()
            }

            val intent = Intent(applicationContext, view_reading::class.java)
            intent.putExtra("record", record_string)
            startActivity(intent)


        }

        val finalHistory_array = history_array
        lv.onItemLongClickListener = AdapterView.OnItemLongClickListener { arg0, arg1, position, arg3 ->
            // on long click prompt user to delete record
            // ListView Clicked item value
            val record = lv.getItemAtPosition(position) as ReadingRecord


            val alert = AlertDialog.Builder(
                    this@MainActivity)
            alert.setTitle("Remove Record")
            alert.setMessage("Are you sure you want to delete this record?")
            alert.setPositiveButton("YES") { dialog, _ ->

                finalHistory_array.remove(record)
                //reverse the order of the list in order to store it in the correct order
                Collections.reverse(finalHistory_array)

                try {
                    history_array_string[0] = MainActivity.toString(finalHistory_array)
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                val editor = sharedPreferences.edit()
                editor.putString("history", history_array_string[0])
                editor.commit()

                //reverse the order again in order to display it correctly
                Collections.reverse(finalHistory_array)
                adapter.notifyDataSetChanged()


                dialog.dismiss()
            }
            alert.setNegativeButton("NO") { dialog, _ -> dialog.dismiss() }
            alert.show()
            true
        }


    }

    private fun save(isChecked: Boolean) {
        val sharedPreferences = getSharedPreferences("me.brereton.iching", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("transitional_check", isChecked)
        editor.commit()
    }

    private fun load(): Boolean {
        val sharedPreferences = getSharedPreferences("me.brereton.iching", Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean("transitional_check", false)
    }

    companion object {

        val EXTRA_MESSAGE = "me.brereton.iching.MESSAGE"


        /** Read the object from Base64 string.  */
        @Throws(IOException::class, ClassNotFoundException::class)
        fun fromString(s: String): Any {
            val data = Base64.decode(s, Base64.DEFAULT)
            val ois = ObjectInputStream(
                    ByteArrayInputStream(data))
            val o = ois.readObject()
            ois.close()
            return o
        }

        /** Write the object to a Base64 string.  */
        @Throws(IOException::class)
        fun toString(o: Serializable): String {
            val baos = ByteArrayOutputStream()
            val oos = ObjectOutputStream(baos)
            oos.writeObject(o)
            oos.close()
            return Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT)
        }
    }

}
