package com.gibsoncodes.newsapp.model

import android.os.Parcel
import android.os.Parcelable


class ArticlePresentation(val author: String?,
                          val title:String?,
                          val description:String?,
                          val url:String?,
                          val urlToImage:String?,
                          val publishedAt:String?,
                          val source: SourcePresentation?
):Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readParcelable(SourcePresentation::class.java.classLoader)
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(author)
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeString(url)
        parcel.writeString(urlToImage)
        parcel.writeString(publishedAt)
        parcel.writeParcelable(source, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ArticlePresentation> {
        override fun createFromParcel(parcel: Parcel): ArticlePresentation {
            return ArticlePresentation(parcel)
        }

        override fun newArray(size: Int): Array<ArticlePresentation?> {
            return arrayOfNulls(size)
        }
    }

}