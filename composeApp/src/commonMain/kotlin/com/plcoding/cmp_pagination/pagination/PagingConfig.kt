package com.plcoding.cmp_pagination.pagination

data class PagingConfig(
    val pageSize: Int,
    val prefetchDistance: Int = pageSize,
    val enablePlaceholders: Boolean = false,
    val initialLoadSize: Int = pageSize,
    val maxSize: Int = Int.MAX_VALUE
)