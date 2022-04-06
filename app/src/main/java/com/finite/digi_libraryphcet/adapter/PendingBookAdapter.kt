package com.finite.digi_libraryphcet.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.findFragment
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.finite.digi_libraryphcet.R
import com.finite.digi_libraryphcet.home.HomeFragmentDirections
import com.finite.digi_libraryphcet.model.BookModel
import com.finite.digi_libraryphcet.model.PendingModel

class PendingBookAdapter(private val bookList : ArrayList<PendingModel>) : RecyclerView.Adapter<PendingBookAdapter.PendingBookViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PendingBookViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.pending_singlerow, parent, false)
        return PendingBookViewHolder(view)
    }

    var onItemClick: ((PendingModel) -> Unit)? = null

    override fun onBindViewHolder(holder: PendingBookViewHolder, position: Int) {
        val currentitem = bookList[position]

        Glide.with(holder.itemView.context).load(currentitem.picurl).centerCrop().into(holder.picImage)

        holder.bookName.text = currentitem.bookName
        holder.authorName.text = currentitem.bookAuthor
        holder.reqName.text = currentitem.reqName

//        holder.horizBookImage.setOnClickListener {
//            val action = HomeFragmentDirections.actionHomeFragmentToBookDetailFragment(currentitem.libCode)
//            findNavController(holder.itemView.findFragment()).navigate(action)
//        }

            holder.itemView.setOnClickListener {
                onItemClick?.invoke(bookList[position])
            }

    }

    override fun getItemCount(): Int {
        return bookList.size
    }

    class PendingBookViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        var bookName : TextView = itemView.findViewById(R.id.bookName)
        var authorName : TextView = itemView.findViewById(R.id.authorName)
        var picImage : ImageView = itemView.findViewById(R.id.picImage)
        var reqName : TextView = itemView.findViewById(R.id.reqBy)
    }
}