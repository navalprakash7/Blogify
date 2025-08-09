package com.example.blogify.models

import android.os.Parcel
import android.os.Parcelable

data class BlogItemModel(
    var heading: String = "",
    var userName: String? = "",
    val date: String = "",
    var post: String = "",
    val userId: String? = "",
    var postId: String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString(),
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(heading)
        parcel.writeString(userName)
        parcel.writeString(date)
        parcel.writeString(post)
        parcel.writeString(userId)
        parcel.writeString(postId)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<BlogItemModel> {
        override fun createFromParcel(parcel: Parcel): BlogItemModel {
            return BlogItemModel(parcel)
        }

        override fun newArray(size: Int): Array<BlogItemModel?> {
            return arrayOfNulls(size)
        }
    }
}
