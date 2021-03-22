package com.rania.useralbum.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.rania.useralbum.R
import com.rania.useralbum.model.User
import com.rania.useralbum.utils.TextUtil

class UserAdapter(private val context: Context, private var dataSet: List<User>) :
    RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: AppCompatTextView
        val email: AppCompatTextView
        val phone: AppCompatTextView
        val website: AppCompatTextView

        init {
            // Define click listener for the ViewHolder's View.
            name = view.findViewById(R.id.userName)
            email = view.findViewById(R.id.userEmail)
            phone = view.findViewById(R.id.userPhone)
            website = view.findViewById(R.id.userWebsite)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_user, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        val name = context.getString(
            R.string.user_item_name,
            dataSet[position].name, dataSet[position].username
        )

        val coloredName = TextUtil.colorText(
            name,
            name.indexOf("(") + 1,
            name.indexOf(")"),
            ContextCompat.getColor(context, R.color.secondaryTextColor)
        )
        viewHolder.name.text = coloredName
        viewHolder.email.text = dataSet[position].email
        viewHolder.phone.text = dataSet[position].phone
        viewHolder.website.text = dataSet[position].website

    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

    fun updateList(newDataSet: List<User>) {
        dataSet = newDataSet
        notifyDataSetChanged()
    }

}