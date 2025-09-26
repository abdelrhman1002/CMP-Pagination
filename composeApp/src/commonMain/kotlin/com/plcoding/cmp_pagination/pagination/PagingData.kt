package com.plcoding.cmp_pagination.pagination

data class PagingData<T>(
    val items: List<T> = emptyList(),
    val isLoading: Boolean = false,
    val error: Throwable? = null,
    val hasMore: Boolean = true,
    val isRefreshing: Boolean = false
)