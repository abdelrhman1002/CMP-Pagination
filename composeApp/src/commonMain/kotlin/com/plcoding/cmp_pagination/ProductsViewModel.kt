package com.plcoding.cmp_pagination

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ProductsState(
    val products: List<ProductDto> = emptyList(),
    val isLoadingMore: Boolean = false,
    val error: String? = null
)

class ProductsViewModel(
    private val api: ProductsApi
): ViewModel() {

    private val _state = MutableStateFlow(ProductsState())
    val state = _state.asStateFlow()

    private val pageSize = 10
    private val paginator = Paginator<Int, ProductResponseDto>(
        initialKey = 0,
        onLoadUpdated = { isLoading ->
            _state.update { it.copy(
                isLoadingMore = isLoading
            ) }
        },
        onRequest = { currentPage ->
            api.getProducts(
                page = currentPage,
                pageSize = pageSize
            )
        },
        getNextKey = { currentPage, _ ->
            currentPage + 1
        },
        onError = { throwable ->
            _state.update { it.copy(
                error = throwable?.message
            ) }
        },
        onSuccess = { productsResponse, nextPage ->
            _state.update { it.copy(
                products = it.products + productsResponse.products,
                error = null
            ) }
        },
        endReached = { currentPage, response ->
            (currentPage * pageSize) >= response.total
        }
    )

    init {
        loadNextItems()
    }

    fun loadNextItems() {
        viewModelScope.launch {
            paginator.loadNextItems()
        }
    }
}