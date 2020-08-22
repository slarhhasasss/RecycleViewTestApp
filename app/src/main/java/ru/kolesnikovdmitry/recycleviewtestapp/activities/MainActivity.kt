package ru.kolesnikovdmitry.recycleviewtestapp.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.rec_view_item_contacts.view.*
import org.json.JSONArray
import org.json.JSONObject
import ru.kolesnikovdmitry.recycleviewtestapp.R
import ru.kolesnikovdmitry.recycleviewtestapp.adapters.ContactsAdapter
import ru.kolesnikovdmitry.recycleviewtestapp.classes.OnRecyclerViewClickListener


//import ru.kolesnikovdmitry.recycleviewtestapp.adapters.ContactsAdapter

class MainActivity: AppCompatActivity() {

    companion object {
        const val JSON_KEY_NAME = "name"
        const val JSON_KEY_NUMBER = "number"
        const val JSON_KEY_POSITION = "position"

        const val REQUEST_CODE_ADD_CONTACT = 101
        const val REQUEST_CODE_EDIT_CONTACT = 102
    }

    private var mIsShowingPopUpMenu = 0
    private var mWasClickOnItem = 0

    private var mContactList : JSONArray = JSONArray()

    private lateinit var mContactsAdapter : ContactsAdapter

    override fun onResume() {
        super.onResume()
        runLayoutAnim(recycleViewContacts)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        setUpRecyclerView()


    }

    private fun setUpRecyclerView() {
        if (mContactList.length() == 0) {
            textViewFlagEmptyRecycleView.visibility = TextView.VISIBLE
        } else {
            textViewFlagEmptyRecycleView.visibility = TextView.INVISIBLE
        }

        mContactsAdapter = ContactsAdapter(mContactList)

        val linearLayoutManager = LinearLayoutManager(this)
        val animator = DefaultItemAnimator()

        recycleViewContacts.adapter = mContactsAdapter
        recycleViewContacts.layoutManager = linearLayoutManager
        recycleViewContacts.itemAnimator = animator

        recycleViewContacts.addOnItemTouchListener(
            OnRecyclerViewClickListener(applicationContext,
            recycleViewContacts, object : OnRecyclerViewClickListener.ClickListener {
                override fun onClick(view: View, position: Int) {
                    if (mWasClickOnItem == 0) {
                        val strName = view.textViewNameContactItem.text.toString()
                        val strNumber = view.textViewNumberContactItem.text.toString()
                        val intent = Intent(applicationContext, EditContactActivity::class.java)
                        intent.putExtra(JSON_KEY_NAME, strName)
                        intent.putExtra(JSON_KEY_NUMBER, strNumber)
                        intent.putExtra(JSON_KEY_POSITION, position.toString())
                        startActivityForResult(intent, REQUEST_CODE_EDIT_CONTACT)
                        mWasClickOnItem = 1
                    }
                }

                override fun onLongClick(view: View, position: Int) {
                    val popUpMenu : PopupMenu = PopupMenu(applicationContext, view)
                    popUpMenu.menu.add(101, 102, 1, "Delete contact")
                    popUpMenu.setOnMenuItemClickListener {
                        when (it.itemId) {
                            102 -> {
                                popUpMenu.dismiss()
                                return@setOnMenuItemClickListener deleteContact(position)
                            }
                        }
                        popUpMenu.dismiss()
                        return@setOnMenuItemClickListener true
                    }
                    popUpMenu.setOnDismissListener {
                        mIsShowingPopUpMenu = 0
                    }
                    if (mIsShowingPopUpMenu == 0) {
                        mIsShowingPopUpMenu = 1
                        popUpMenu.show()
                    }
                }
            })
        )
    }

    private fun deleteContact(position: Int): Boolean {
        mContactList.remove(position)

        //Данное действие позволяет удалить с омощью анимации элемент списка, не перезагружая весь список
        recycleViewContacts.adapter?.notifyItemRemoved(position)
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menuItemAddNewContactActMain -> {
                val intent = Intent(applicationContext, AddContactActivity::class.java)
                startActivityForResult(intent, REQUEST_CODE_ADD_CONTACT)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode) {
            REQUEST_CODE_ADD_CONTACT -> {
                when(resultCode) {
                    Activity.RESULT_CANCELED -> {
                        Snackbar.make(textViewFlagEmptyRecycleView, getString(R.string.nothing_was_added), Snackbar.LENGTH_LONG).show()
                    }
                    Activity.RESULT_OK -> {
                        addNewContact(data)
                    }
                }
            }
            REQUEST_CODE_EDIT_CONTACT -> {
                mWasClickOnItem = 0
                when(resultCode) {
                    Activity.RESULT_OK -> {
                        Toast.makeText(applicationContext, "Saved", Toast.LENGTH_SHORT).show()
                        updateContactInfo(data)
                    }
                    Activity.RESULT_CANCELED -> {
                        Toast.makeText(applicationContext, "Not saved", Toast.LENGTH_SHORT).show()
                    }
                    EditContactActivity.RESULT_DELETE -> {
                        val itemPosition : Int = data?.getStringExtra(JSON_KEY_POSITION)!!.toInt()
                        mContactList.remove(itemPosition)
                        setUpRecyclerView()
                    }
                }
            }
        }
    }

    private fun runLayoutAnim(recView: RecyclerView) {
        val context = recView.context
        val layAnimController =
            AnimationUtils.loadLayoutAnimation(context, R.anim.layout_anim_fall_dawn)

        recView.layoutAnimation = layAnimController
        recView.adapter?.notifyDataSetChanged()
        recView.scheduleLayoutAnimation()
    }

    private fun updateContactInfo(data: Intent?) {
        val newJSONObj = JSONObject()
        val strName = data?.getStringExtra(JSON_KEY_NAME)
        val strNumber = data?.getStringExtra(JSON_KEY_NUMBER)
        val curPos = data?.getStringExtra(JSON_KEY_POSITION)!!.toInt()
        newJSONObj.put(JSON_KEY_NAME, strName)
        newJSONObj.put(JSON_KEY_NUMBER, strNumber)
        mContactList.put(curPos, newJSONObj)
    }

    private fun addNewContact(data: Intent?) {
        val jsonObjectContact = JSONObject()
        val strName = data!!.getStringExtra(JSON_KEY_NAME)
        val strNumber = data.getStringExtra(JSON_KEY_NUMBER)
        jsonObjectContact.put(JSON_KEY_NAME, strName)
        jsonObjectContact.put(JSON_KEY_NUMBER, strNumber)
        try {
            mContactList.put(jsonObjectContact)
        } catch (th : Throwable) {
            Toast.makeText(applicationContext, th.message, Toast.LENGTH_LONG).show()
        }


        setUpRecyclerView()
    }
}