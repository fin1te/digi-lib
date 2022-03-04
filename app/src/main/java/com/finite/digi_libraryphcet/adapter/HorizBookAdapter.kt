package com.finite.digi_libraryphcet.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.finite.digi_libraryphcet.R
import com.finite.digi_libraryphcet.model.BookModel

class HorizBookAdapter(private val bookList : ArrayList<BookModel>) : RecyclerView.Adapter<HorizBookAdapter.HorizBookViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HorizBookViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.horizontal_book_singlerow, parent, false)
        return HorizBookViewHolder(view)
    }

    override fun onBindViewHolder(holder: HorizBookViewHolder, position: Int) {
        val currentitem = bookList[position]

        Glide.with(holder.itemView.context).load(currentitem.picurl).centerCrop().into(holder.horizBookImage)
    }

    override fun getItemCount(): Int {
        return bookList.size
    }

    class HorizBookViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        var horizBookImage : ImageView = itemView.findViewById(R.id.horizBookImage)
    }
}