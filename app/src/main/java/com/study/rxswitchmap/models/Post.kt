package com.study.rxjavastudy.models

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class Post(
    @SerializedName("userId") @Expose val userId: Int, @SerializedName("id") @Expose val id: Int
    , @SerializedName("title") @Expose val title: String, @SerializedName("body") @Expose val body: String) : Parcelable {


    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!
    )


    override fun toString(): String {
        return "Post{" +
                "userId=" + userId +
                ", id=" + id +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                '}'
    }

    override fun writeToParcel(p0: Parcel?, p1: Int) {
        p0?.writeInt(userId)
        p0?.writeInt(id)
        p0?.writeString(title)
        p0?.writeString(body)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Post> {
        override fun createFromParcel(parcel: Parcel): Post {
            return Post(parcel)
        }

        override fun newArray(size: Int): Array<Post?> {
            return arrayOfNulls(size)
        }
    }
}