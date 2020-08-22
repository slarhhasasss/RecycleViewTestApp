package ru.kolesnikovdmitry.recycleviewtestapp.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONArray
import ru.kolesnikovdmitry.recycleviewtestapp.R
import ru.kolesnikovdmitry.recycleviewtestapp.activities.AddContactActivity
import ru.kolesnikovdmitry.recycleviewtestapp.activities.MainActivity

//Создание адаптера. Оно всегда однотипное:
class ContactsAdapter(private val mContactsArray : JSONArray) :
    RecyclerView.Adapter<ContactsAdapter.ContactsViewHolder>() {

    lateinit var mParentContext : Context

    //здесь инициализируем поля, чтобы потом в onBindViewHolder() можно было к ним обращаться и заполнять их
    class ContactsViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val textViewContactNameRecViewItem: TextView =
            itemView.findViewById<TextView>(R.id.textViewNameContactItem)
        val textViewContactNumberRecViewItem: TextView =
            itemView.findViewById<TextView>(R.id.textViewNumberContactItem)
        val constrLayContact: ConstraintLayout = itemView.findViewById(R.id.constrLayRecViewItemContacts)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.rec_view_item_contacts, parent, false)
        mParentContext = parent.context
        return ContactsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mContactsArray.length()
    }

    //заполняем поля здесь
    override fun onBindViewHolder(holder: ContactsViewHolder, position: Int) {
        val jsonItem = mContactsArray.getJSONObject(position)
        val contactName : String = jsonItem.getString(MainActivity.JSON_KEY_NAME)
        val contactNumber : String = jsonItem.getString(MainActivity.JSON_KEY_NUMBER)

        holder.textViewContactNameRecViewItem.text = contactName
        holder.textViewContactNumberRecViewItem.text = contactNumber
    }


}
