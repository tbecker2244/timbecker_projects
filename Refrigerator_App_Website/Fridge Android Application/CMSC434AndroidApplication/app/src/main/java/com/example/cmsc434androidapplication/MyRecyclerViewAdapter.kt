package com.example.cmsc434androidapplication

import android.app.AlertDialog
import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView

class MyRecyclerViewAdapter(
          //  private val name: String,
         //   private val type:String,
          //  private val quanity: String,
        public var mNames: ArrayList<String>,
        public var mTypes: ArrayList<String>,
        public var mQuantity: ArrayList<String>,
        public val mRowLayout: Int,
        val cont: Context
) : RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder>() {

    // Create ViewHolder which holds a View to be displayed
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(mRowLayout, viewGroup, false)
        return ViewHolder(v)
    }

    // Binding: The process of preparing a child view to display data corresponding to a position within the adapter.
    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {

        viewHolder.mName.text = mNames[i]
        viewHolder.mType.text = mTypes[i]
        viewHolder.mQuantity.text = mQuantity[i]

       viewHolder.deleteButton.setOnClickListener(){
           mNames.remove(viewHolder.mName.text.toString())
           mTypes.remove(viewHolder.mType.text.toString())
           mQuantity.remove(viewHolder.mQuantity.text.toString())
           notifyDataSetChanged()
       }

        viewHolder.editButton.setOnClickListener(){

            val v = LayoutInflater.from(viewHolder.editButton.context).inflate(R.layout.update_item, null, false)
            val res: Resources = viewHolder.itemView.context.resources

            val userInputFood: EditText = v.findViewById(R.id.updateTextFoodItem)
            val userInputType: EditText = v.findViewById(R.id.updateTextFoodType)
           val userInputQuantity: EditText = v.findViewById(R.id.updateTextQuantity)

           val alertDialog2 = AlertDialog.Builder(viewHolder.context)


            alertDialog2.setView(R.layout.update_item)
            alertDialog2.setPositiveButton("Enter") { dialog, which ->

                val name = userInputFood.text.toString()
                Toast.makeText(viewHolder.context, userInputFood.text, Toast.LENGTH_SHORT).show()

                viewHolder.mName.text = name

                mNames.set(i, "Milk")
                mTypes.set(i, "Dairy")
                mQuantity.set(i, "2")

                notifyItemChanged(i)

            }
            alertDialog2.setNeutralButton("Cancel"){dialog, which ->
                dialog.cancel()
            }


            val alert = alertDialog2.create()
            alert.show()

            val button: Button = alert.getButton(AlertDialog.BUTTON_NEUTRAL)
            val buttonEnt: Button = alert.getButton(AlertDialog.BUTTON_POSITIVE)
            button.setBackgroundColor(res.getColor(R.color.red))
            buttonEnt.setBackgroundColor(res.getColor(R.color.green))

        }

    }

    override fun getItemCount(): Int {
        return mNames.size;
    }

     class ViewHolder public constructor(itemView: View) : RecyclerView.ViewHolder(itemView),
            View.OnClickListener {
        internal val mName: TextView = itemView.findViewById(R.id.food_name)
        internal val mType: TextView = itemView.findViewById(R.id.food_type)
        internal val mQuantity: TextView = itemView.findViewById(R.id.food_quantity)
         internal val deleteButton: ImageButton = itemView.findViewById(R.id.trash_can)
         internal val editButton: ImageButton = itemView.findViewById(R.id.edit)
         internal val context = itemView.context

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            // Display a Toast message indicting the selected item

        }


    }


}

