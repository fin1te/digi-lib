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

class BookAdapter(private val bookList : ArrayList<BookModel>) : RecyclerView.Adapter<BookAdapter.BookViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BookViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.book_singlerow, parent, false)
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val currentitem = bookList[position]

        holder.bookTitle.text = currentitem.bookName
        holder.bookAuthor.text = "By  ${currentitem.author}"
        holder.bookShelf.text = "Shelf no: ${currentitem.shelfNo}"
        holder.bookNoOfCopies.text = "Available: ${currentitem.noCopies} copies"
        Glide.with(holder.itemView.context).load(currentitem.picurl).centerCrop().into(holder.bookImg)
    }

    override fun getItemCount(): Int {
        return bookList.size
    }

    class BookViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        var bookTitle : TextView = itemView.findViewById(R.id.bookTitle)
        var bookAuthor: TextView = itemView.findViewById(R.id.bookAuthor)
        var bookNoOfCopies: TextView = itemView.findViewById(R.id.bookNoOfCopies)
        var bookShelf: TextView = itemView.findViewById(R.id.bookShelf)
        var bookImg: ImageView = itemView.findViewById(R.id.bookImg)
    }
}