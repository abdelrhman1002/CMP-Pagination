package com.plcoding.cmp_pagination.remote


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductResponse(
    @SerialName("content")
    val content: List<Content> = listOf(),
    @SerialName("empty")
    val empty: Boolean = false,
    @SerialName("first")
    val first: Boolean = false,
    @SerialName("last")
    val last: Boolean = false,
    @SerialName("number")
    val number: Int = 0,
    @SerialName("numberOfElements")
    val numberOfElements: Int = 0,
    @SerialName("pageable")
    val pageable: Pageable = Pageable(),
    @SerialName("size")
    val size: Int = 0,
    @SerialName("sort")
    val sort: SortX = SortX(),
    @SerialName("totalElements")
    val totalElements: Int = 0,
    @SerialName("totalPages")
    val totalPages: Int = 0
)