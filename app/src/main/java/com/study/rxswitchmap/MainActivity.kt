package com.study.rxswitchmap

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.study.rxjavastudy.adapter.RecyclerAdapter
import com.study.rxjavastudy.models.Post
import com.study.rxjavastudy.retrofitservices.ServiceGenerator
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Function
import io.reactivex.functions.Predicate
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.http.POST
import java.util.ArrayList
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity(), RecyclerAdapter.OnPostClickListner{

    var recyclerView : RecyclerView? = null
    var recyclerAdapter : RecyclerAdapter? = null
    val compositeDisposable: CompositeDisposable = CompositeDisposable()
    val TAG = "MainActivity"
    val publishSubject : PublishSubject<Post> = PublishSubject.create()

    val PERIOD : Long = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.recycler_view)
        initRcycleView()
        retrivePosts()

    }

    fun retrivePosts(){
        ServiceGenerator.getRequestApi().posts
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<List<Post>>{
                override fun onComplete() {
                    Log.d(TAG, "onCompete() get post list")
                }

                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                }

                override fun onNext(t: List<Post>) {
                    recyclerAdapter?.setPosts(ArrayList(t))
                }

                override fun onError(e: Throwable) {
                    e.printStackTrace()
                }
            })
    }

    override fun onPostClick(position: Int) {
        Log.d(TAG, "onPostClick: clicked.");

        //submit the selected post object for query
        publishSubject.onNext(recyclerAdapter?.getposts()!![position])
    }

    private fun navToActivity(post : Post){
        val intent = Intent(applicationContext, ViewPostActivity :: class.java)
        intent.putExtra("name", post)
        startActivity(intent)
    }

    fun initRcycleView(){
        recyclerAdapter = RecyclerAdapter(this)
        recyclerView!!.layoutManager = LinearLayoutManager(this)
        recyclerView!!.adapter = recyclerAdapter
    }

    override fun onResume() {
        super.onResume()
        progress_bar.setProgress(0)
        initSwitchMapDemo()
    }

    override fun onPause() {
        Log.d(TAG, "onPause() Called!!")
        compositeDisposable.clear()
        super.onPause()
    }

    fun initSwitchMapDemo(){
        val postMappinFunction : Function<Post, ObservableSource<Post>> = Function {
            val recMappinFunction : Function<Long, ObservableSource<Post>> = Function {
                ServiceGenerator.getRequestApi().getPost(it.toInt())
            }
            Observable.interval(PERIOD, TimeUnit.MILLISECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .takeWhile({
                    Log.d(TAG, "test: " + Thread.currentThread().getName() + ", " + it);
                    progress_bar.max =  (5000 - PERIOD).toInt()
                    progress_bar.setProgress(((it * PERIOD) + PERIOD).toInt())
                    it <= 5000/PERIOD
                })
                .filter(Predicate {
                    it >= 5000/PERIOD
                })
                .subscribeOn(Schedulers.io())
                .flatMap(recMappinFunction)
        }

        publishSubject.switchMap(postMappinFunction)
            .subscribe(object : Observer<Post> {
                override fun onComplete() {
                }

                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                }

                override fun onNext(t: Post) {
                    navToActivity(t)
                }

                override fun onError(e: Throwable) {
                    e.printStackTrace()
                }
            })

    }

}
