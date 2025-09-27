package com.plcoding.cmp_pagination.remote


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Pageable(
    @SerialName("offset")
    val offset: Int = 0,
    @SerialName("pageNumber")
    val pageNumber: Int = 0,
    @SerialName("pageSize")
    val pageSize: Int = 0,
    @SerialName("paged")
    val paged: Boolean = false,
    @SerialName("sort")
    val sort: SortX = SortX(),
    @SerialName("unpaged")
    val unpaged: Boolean = false
)