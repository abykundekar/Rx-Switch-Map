package com.study.rxswitchmap.networkcall;


import com.study.rxjavastudy.models.Post;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RequestApi {

    @GET("posts")
    Observable<List<Post>> getPosts();//Get the list of posts

    @GET("posts/{id}")
    Observable<Post> getPost(@Path("id") int id);

}
