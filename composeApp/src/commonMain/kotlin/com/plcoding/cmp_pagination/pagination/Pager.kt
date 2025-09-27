package com.plcoding.cmp_pagination.pagination

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class Pager<Key : Any, Value : Any>(
    private val config: PagingConfig = PagingConfig(),
    private val pagingSourceFactory: () -> PagingSource<Key, Value>
) {
    private val _flow = MutableStateFlow(PagingData<Value>())
    val flow: Flow<PagingData<Value>> = _flow.asStateFlow()

    private var currentPagingSource: PagingSource<Key, Value>? = null
    private var currentKey: Key? = null
    private var loadedItemsCount = 0

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
                    val newItems = if (isRefresh) result.data
                    else _flow.value.items + result.data

                    val finalItems =
                        if (config.maxSize != Int.MAX_VALUE && newItems.size > config.maxSize) {
                            newItems.takeLast(config.maxSize)
                        } else {
                            newItems
                        }

                    currentKey = result.nextKey
                    loadedItemsCount = finalItems.size

                    _flow.value = _flow.value.copy(
                        items = finalItems,
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

    fun shouldLoadMore(lastVisibleIndex: Int, totalItems: Int): Boolean {
        val remainingItems = totalItems - lastVisibleIndex
        return remainingItems <= config.prefetchDistance &&
                _flow.value.hasMore &&
                _flow.value.isLoading.not()
    }

    suspend fun refresh() {
        _flow.value = _flow.value.copy(isRefreshing = true)
        currentKey = null
        currentPagingSource = null
        loadedItemsCount = 0
        load()
    }
}