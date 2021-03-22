package com.rania.useralbum.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.rania.useralbum.R
import com.rania.useralbum.model.Photo
import com.squareup.picasso.Picasso

class PhotoAdapter(private val context: Context, private var dataSet: List<Photo>) :
    RecyclerView.Adapter<PhotoAdapter.ViewHolder>() {

    var onItemClick: ((Photo) -> Unit)? = null

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: AppCompatTextView
        val thumb: AppCompatImageView

        init {
            // Define click listener for the ViewHolder's View.
            title = view.findViewById(R.id.photoTitle)
            thumb = view.findViewById(R.id.photoThumb)

            itemView.setOnClickListener {
                onItemClick?.invoke(dataSet[adapterPosition])
            }
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_photo, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.title.text = dataSet[position].title
        Picasso.get()
            .load(Uri.parse(dataSet[position].thumbnailUrl))
            .resizeDimen(R.dimen.image_view_size_150, R.dimen.image_view_size_150)
            .centerCrop()
            .tag(context)
            .into(viewHolder.thumb)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

    fun updateList(newDataSet: List<Photo>) {
        dataSet = newDataSet
        notifyDataSetChanged()
    }

}