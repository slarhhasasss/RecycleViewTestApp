package ru.kolesnikovdmitry.recycleviewtestapp.classes

import android.content.Context
import android.view.ContextMenu
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import java.text.ParsePosition

class OnRecyclerViewClickListener(
    val context : Context,
    val recView: RecyclerView,
    val clickListener: ClickListener?
): RecyclerView.OnItemTouchListener {

    private lateinit var mGestureDetector: GestureDetector

    init {
        mGestureDetector = GestureDetector(context, object :
            GestureDetector.SimpleOnGestureListener() {

            override fun onSingleTapUp(e: MotionEvent?): Boolean {
                return true
            }

            override fun onLongPress(e: MotionEvent?) {
                val childView : View? = recView.findChildViewUnder(e!!.x, e.y)

                if (childView != null && clickListener != null) {
                    clickListener.onLongClick(childView, recView.getChildAdapterPosition(childView))
                }
            }
        })
    }

    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
        TODO("Not yet implemented")
    }

    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
        val childView: View? = rv.findChildViewUnder(e.x, e.y)

        if (childView != null && clickListener != null && mGestureDetector.onTouchEvent(e)) {
            clickListener.onClick(childView, rv.getChildAdapterPosition(childView))
        }
        return false
    }

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
        TODO("Not yet implemented")
    }

    public interface ClickListener {

        fun onClick(view: View, position: Int)

        fun onLongClick(view: View, position: Int)

    }
}


/*
Это решение допускает множественное срабатывание onClick и onLongClick при нажатии, что не всегда круто и легко исправить.
recycleViewContacts.addOnItemTouchListener(OnRecyclerViewClickListener(applicationContext,
            recycleViewContacts, object : OnRecyclerViewClickListener.ClickListener {
                override fun onClick(view: View, position: Int) {
                   //Your code here
                }

                override fun onLongClick(view: View, position: Int) {
                    //Your code here
                }
            }))
 */

