package com.example.semester_project

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.RelativeLayout
import android.widget.TextView
import java.util.ArrayList

//Adapter used to handle location list shown in tour detail page and edit page
class LocationListAdapter(private val mContext: Context, tourId: String) : BaseAdapter() {

    private val mTour = ArrayList<Location>()
    private val tourId = tourId
    private var locationIds = ArrayList<String>()

    fun add(location: Location, id: String) {
        mTour.add(location)
        locationIds.add(id)
        notifyDataSetChanged()
    }

    fun remove(pos: Int) {
        mTour.removeAt(pos)
        locationIds.removeAt(pos)
        notifyDataSetChanged()
    }

    fun clear() {
        mTour.clear()
        notifyDataSetChanged()
    }

    fun getTourId(): String {
        return tourId
    }

    fun getLocationId(pos: Int): String {
        return locationIds[pos]
    }

    fun getLocations(): ArrayList<String> {
        val result = ArrayList<String>()
        for (location in mTour)
            result.add(location.address)
        return result
    }

    fun getNames(): ArrayList<String> {
        val result = ArrayList<String>()
        for (location in mTour)
            result.add(location.name)
        return result
    }

    fun getDescriptions(): ArrayList<String> {
        val result = ArrayList<String>()
        for (location in mTour)
            result.add(location.description)
        return result
    }

    override fun getCount(): Int {
        return mTour.size
    }

    override fun getItem(pos: Int): Location {
        val location = mTour[pos]
        return Location(location.name, location.address, location.description, location.images)
    }

    override fun getItemId(pos: Int): Long {
        return pos.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        val location = getItem(position)
        val viewHolder: ViewHolder

        if (null == convertView) {
            viewHolder = ViewHolder()

            val newView = LayoutInflater.from(mContext).inflate(R.layout.location_card, parent, false)
            viewHolder.name = newView.findViewById(R.id.locationName)
            viewHolder.address = newView.findViewById(R.id.locationAddress)
            viewHolder.description = newView.findViewById(R.id.locationDesc)
            viewHolder.mItemLayout = newView.findViewById(R.id.locationRelativeLayout)
            newView.setTag(viewHolder)

        } else {
            viewHolder = convertView.tag as ViewHolder
        }

        viewHolder.position = position
        viewHolder.name!!.text = location.name
        viewHolder.address!!.text = location.address
        viewHolder.description!!.text = location.description

        return viewHolder.mItemLayout
    }

    fun adjust(location: Location, pos: Int) {
        if (pos == -1)
            return
        mTour.removeAt(pos)
        mTour.add(pos, location)
        notifyDataSetChanged()
    }

    internal class ViewHolder {
        var position: Int = 0
        var name: TextView? = null
        var address: TextView? = null
        var description: TextView? = null
        var mItemLayout: RelativeLayout? = null
    }

    companion object {
        private const val LOCATION_ADDRESS = "LOCATION_ADDRESS"
        private const val IMAGES = "IMAGES"
        private const val DESCRIPTION = "DESCRIPTION"
        private const val NAME = "NAME"

    }

}