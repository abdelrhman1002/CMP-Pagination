package com.plcoding.cmp_pagination.remote


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SortX(
    @SerialName("empty")
    val empty: Boolean = false,
    @SerialName("sorted")
    val sorted: Boolean = false,
    @SerialName("unsorted")
    val unsorted: Boolean = false
)