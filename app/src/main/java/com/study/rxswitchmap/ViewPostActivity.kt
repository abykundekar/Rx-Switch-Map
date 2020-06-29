package com.study.rxswitchmap

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.study.rxjavastudy.models.Post
import kotlinx.android.synthetic.main.activity_view_post.*

class ViewPostActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_post)

        getIncomingIntent()
    }

    fun getIncomingIntent(){
        if(intent.hasExtra("post")){
            val post : Post = intent.getParcelableExtra("post")
            textView.text = post.title
        }
    }
}
