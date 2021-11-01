package com.example.semester_project

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import kotlin.collections.ArrayList


// The Adapter used to show the Favorate list shown in user page
class FavorateListAdapter(private val mContext: Context) : BaseAdapter() {

    private val mTour = ArrayList<Tour>()
    private val tourId = ArrayList<String>()

    fun add(tour: Tour, id: String) {
        mTour.add(tour)
        tourId.add(id)
        notifyDataSetChanged()
    }

    fun clear() {
        mTour.clear()
        tourId.clear()
        notifyDataSetChanged()
    }

    override fun getCount(): Int {
        return mTour.size
    }

    override fun getItem(pos: Int): Tour {
        return mTour[pos]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun getTourId(pos: Int): String {
        return tourId[pos]
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        val tour = getItem(position)
        val viewHolder: ViewHolder

        if (null == convertView) {
            viewHolder = ViewHolder()

            val newView = LayoutInflater.from(mContext).inflate(R.layout.favorite_tour, parent, false)
            viewHolder.name = newView.findViewById(R.id.tourName)
            viewHolder.author = newView.findViewById(R.id.tourAuthor)
            viewHolder.description = newView.findViewById(R.id.tourDesc)
            viewHolder.mItemLayout = newView.findViewById(R.id.tourRelativeLayout)
            newView.setTag(viewHolder)

        } else {
            viewHolder = convertView.tag as ViewHolder
        }

        viewHolder.position = position
        viewHolder.name!!.text = tour.name
        viewHolder.author!!.text = tour.author
        viewHolder.description!!.text = tour.description

        return viewHolder.mItemLayout
    }


    internal class ViewHolder {
        var position: Int = 0
        var name: TextView? = null
        var author: TextView? = null
        var description: TextView? = null
        var mItemLayout: RelativeLayout? = null
    }

}