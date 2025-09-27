package com.plcoding.cmp_pagination.pagination

data class PagingConfig(
    val pageSize: Int = 20,
    val prefetchDistance: Int = pageSize,
    val enablePlaceholders: Boolean = false,
    val maxSize: Int = Int.MAX_VALUE
)