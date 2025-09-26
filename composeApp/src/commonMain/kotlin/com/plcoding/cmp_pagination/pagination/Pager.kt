package com.plcoding.cmp_pagination.pagination

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class Pager<Key : Any, Value : Any>(
    private val config: PagingConfig,
    private val pagingSourceFactory: () -> PagingSource<Key, Value>
) {
    private val _flow = MutableStateFlow(PagingData<Value>())
    val flow: Flow<PagingData<Value>> = _flow.asStateFlow()

    private var currentPagingSource: PagingSource<Key, Value>? = null
    private var currentKey: Key? = null

    suspend fun load(key: Key? = null) {
        if (_flow.value.isLoading) return

        _flow.value = _flow.value.copy(isLoading = true, error = null)

        try {
            val pagingSource = currentPagingSource ?: pagingSourceFactory().also {
                currentPagingSource = it
            }

            val params = PagingSource.LoadParams(
                key = key ?: currentKey,
                loadSize = config.pageSize
            )

            when (val result = pagingSource.load(params)) {
                is PagingSource.LoadResult.Page -> {
                    val isRefresh = key == null && currentKey == null
                    val newItems = if (isRefresh) {
                        result.data
                    } else {
                        _flow.value.items + result.data
                    }

                    currentKey = result.nextKey

                    _flow.value = _flow.value.copy(
                        items = newItems,
                        isLoading = false,
                        error = null,
                        hasMore = result.nextKey != null,
                        isRefreshing = false
                    )
                }

                is PagingSource.LoadResult.Error -> {
                    _flow.value = _flow.value.copy(
                        isLoading = false,
                        error = result.throwable,
                        isRefreshing = false
                    )
                }
            }
        } catch (e: Exception) {
            _flow.value = _flow.value.copy(
                isLoading = false,
                error = e,
                isRefreshing = false
            )
        }
    }

    suspend fun refresh() {
        _flow.value = _flow.value.copy(isRefreshing = true)
        currentKey = null
        currentPagingSource = null
        load()
    }

    suspend fun retry() {
        if (_flow.value.error != null) {
            load(currentKey)
        }
    }
}