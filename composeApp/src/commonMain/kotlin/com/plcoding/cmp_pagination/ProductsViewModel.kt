package com.plcoding.cmp_pagination

import androidx.lifecycle.viewModelScope
import com.plcoding.cmp_pagination.pagination.PagedFetchResponse
import com.plcoding.cmp_pagination.pagination.PagingData
import com.plcoding.cmp_pagination.pagination.createPagingSource
import kotlinx.coroutines.launch

data class ProductsState(
    val movies : PagingData<Product> = PagingData(),
)

class ProductsViewModel(
    private val getAllProductsUseCase: GetAllProductsUseCase
) : BaseViewModel<ProductsState, ProductsEffect>(
    initialState = ProductsState()
) {

    val pager = createPagingSource { pageNumber ->
        val response = getAllProductsUseCase(pageNumber)
        PagedFetchResponse(
            items = response.content,
            currentPage = pageNumber,
            totalPages = response.totalPages,
        )
    }

    init {
        loadMovies()
    }

    private fun loadMovies() {
        tryToCollect(
            block = { pager.flow },
            onCollect = { pagingData ->
                updateState { copy(movies = pagingData) }
            },
            onError = { exception ->
                updateState {
                    copy(movies = movies.copy(error = exception, isLoading = false))
                }
            }
        )

        viewModelScope.launch {
            pager.load()
        }
    }

    fun loadNextPage() {
        viewModelScope.launch {
            pager.load()
        }
    }

    fun refresh() {
        viewModelScope.launch {
            pager.refresh()
        }
    }
}


sealed class ProductsEffect {
    object NavigateToAddDukanScreen : ProductsEffect()
    object NavigateToPendingDukanScreen : ProductsEffect()
}