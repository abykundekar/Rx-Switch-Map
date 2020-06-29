package com.study.rxjavastudy.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.study.rxjavastudy.models.Post
import com.study.rxswitchmap.R
import org.jetbrains.annotations.NotNull

class RecyclerAdapter(onPostClickListner: OnPostClickListner) : RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>() {
     val TAG = "RecyclerAdapter"
    private var posts : ArrayList<Post>? = ArrayList()
    var onPostClickListner : OnPostClickListner = onPostClickListner

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view : View = LayoutInflater.from(parent.context).inflate(R.layout.layout_post_list_item,null,  false)
        return MyViewHolder(view, this.onPostClickListner)
    }

    override fun onBindViewHolder(@NotNull holder: MyViewHolder, position: Int) {
        holder.bind(posts!!.get(position))
    }

    override fun getItemCount(): Int {
        return if (this.posts?.size != null) {
            this.posts?.size!!
        } else throw NullPointerException("Expression 'posts?.size' must not be null")
    }

    fun setPosts(posts : ArrayList<Post>){
        this.posts  = posts
        notifyDataSetChanged()
    }

    fun updatePost(post : Post){
        this.posts?.set(this.posts!!.indexOf(post), post)
        notifyItemChanged(posts!!.indexOf(post))
    }

    fun getposts() : ArrayList<Post>? {
        return this.posts
    }

    class MyViewHolder(itemView: View, onPostClickListner: OnPostClickListner) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var onPostClickListner : OnPostClickListner = onPostClickListner
        val title = itemView.findViewById<TextView>(R.id.title)
        val progressBar = itemView.findViewById<ProgressBar>(R.id.progress_bar)
        init {
            title.setOnClickListener(this)
        }
        fun bind(post : Post){
            title.setText(post.title)
        }

        override fun onClick(p0: View?) {
            onPostClickListner.onPostClick(adapterPosition)
        }
    }

    interface OnPostClickListner{
        fun onPostClick(position: Int)
    }
}


