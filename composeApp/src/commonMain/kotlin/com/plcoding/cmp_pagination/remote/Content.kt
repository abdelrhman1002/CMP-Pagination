package com.plcoding.cmp_pagination.remote


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Content(
    @SerialName("createdAt")
    val createdAt: String = "",
    @SerialName("description")
    val description: String = "",
    @SerialName("dukanId")
    val dukanId: String = "",
    @SerialName("id")
    val id: String = "",
    @SerialName("imageUrls")
    val imageUrls: List<String> = listOf(),
    @SerialName("name")
    val name: String = "",
    @SerialName("price")
    val price: Double = 0.0,
    @SerialName("shelfId")
    val shelfId: String = ""
)