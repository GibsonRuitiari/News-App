package com.gibsoncodes.newsapp.model

import android.os.Parcel
import android.os.Parcelable


data class SourcePresentation ( val id:String?,
                                val name:String?):Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SourcePresentation> {
        override fun createFromParcel(parcel: Parcel): SourcePresentation {
            return SourcePresentation(parcel)
        }

        override fun newArray(size: Int): Array<SourcePresentation?> {
            return arrayOfNulls(size)
        }
    }

}