package ru.kolesnikovdmitry.recycleviewtestapp.activities

import android.app.Activity
import android.app.IntentService
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_add_contact.*
import kotlinx.android.synthetic.main.activity_main.*
import ru.kolesnikovdmitry.recycleviewtestapp.R

class AddContactActivity: AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_add_contact)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeButtonEnabled(true)



        btnAddActAddContact.setOnClickListener{btn ->
            if(editTextNameActAddContact.text.toString() == "") {
                Toast.makeText(applicationContext, getString(R.string.enter_name), Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if(editTextNumberActAddContact.text.toString() == "") {
                Toast.makeText(applicationContext, getString(R.string.enter_number), Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            addContact()
        }
    }

    private fun addContact() {
        val strName = editTextNameActAddContact.text.toString()
        val strNumber = editTextNumberActAddContact.text.toString()
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra(MainActivity.JSON_KEY_NAME, strName)
        intent.putExtra(MainActivity.JSON_KEY_NUMBER, strNumber)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        //отправляем результат открытия активности как отмена
        setResult(Activity.RESULT_CANCELED)
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                //тправляем результат открытия активности как отмена
                setResult(Activity.RESULT_CANCELED)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}