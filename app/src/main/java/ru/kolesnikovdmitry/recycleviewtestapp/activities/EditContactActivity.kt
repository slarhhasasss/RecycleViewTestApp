package ru.kolesnikovdmitry.recycleviewtestapp.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_add_contact.*
import kotlinx.android.synthetic.main.activity_edit_contact.*
import ru.kolesnikovdmitry.recycleviewtestapp.R

class EditContactActivity: AppCompatActivity() {

    companion object {
        const val RESULT_DELETE = 104
    }

    var mItemPosition : String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_contact)

        val strName : String? = intent.getStringExtra(MainActivity.JSON_KEY_NAME)
        val strNumber : String? = intent!!.getStringExtra(MainActivity.JSON_KEY_NUMBER)
        mItemPosition = intent!!.getStringExtra(MainActivity.JSON_KEY_POSITION)

        supportActionBar!!.title = "Edit Contact"

        editTextNameActEditContact.setText(strName)
        editTextNumberActEditContact.setText(strNumber)

        btnAddActEditContact.setOnClickListener { btn ->
            val strNameNew = editTextNameActEditContact.text.toString()
            val strNumberNew = editTextNumberActEditContact.text.toString()
            if(strNameNew == "") {
                Toast.makeText(applicationContext, getString(R.string.enter_name), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(strNumberNew == "") {
                Toast.makeText(applicationContext, getString(R.string.enter_number), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val intent = Intent(applicationContext, MainActivity::class.java)
            intent.putExtra(MainActivity.JSON_KEY_NAME, strNameNew)
            intent.putExtra(MainActivity.JSON_KEY_NUMBER, strNumberNew)
            intent.putExtra(MainActivity.JSON_KEY_POSITION, mItemPosition)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_edit_act, menu)
        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                setResult(Activity.RESULT_CANCELED)
                finish()
            }
            R.id.menuItemDeleteActEditContact -> {
                val intent = Intent(applicationContext, MainActivity::class.java)
                intent.putExtra(MainActivity.JSON_KEY_POSITION, mItemPosition)
                setResult(RESULT_DELETE, intent)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        setResult(Activity.RESULT_CANCELED)
        finish()
    }
}