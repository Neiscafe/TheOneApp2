package com.example.theoneapp.model

import com.google.gson.annotations.SerializedName

data class Chapter(
    @SerializedName("_id") val _id: String,
    @SerializedName("chapterName") val chapterName: String
) {

}
