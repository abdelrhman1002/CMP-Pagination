package com.plcoding.cmp_pagination

import androidx.lifecycle.viewModelScope
import com.plcoding.cmp_pagination.pagination.PagedFetchResponse
import com.plcoding.cmp_pagination.pagination.createPagingSource
import kotlinx.coroutines.launch

data class ProductsState(
    val movies: List<MovieEntity> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val hasMore: Boolean = true
)

class ProductsViewModel(
    private val getAllProductsUseCase: GetAllProductsUseCase
) : BaseViewModel<ProductsState, ProductsEffect>(
    initialState = ProductsState()
) {

    private val pager = createPagingSource { pageNumber ->
        val response = getAllProductsUseCase(pageNumber)
        PagedFetchResponse(
            items = response.movieDetailApi,
            currentPage = pageNumber,
            totalPages = response.totalPages,
        )
    }

    init {
        loadMovies()
    }

    fun loadMovies() {
        tryToCollect(
            block = { pager.flow },
            onCollect = { pagingData ->
                updateState {
                    copy(
                        movies = pagingData.items,
                        isLoading = pagingData.isLoading,
                        error = pagingData.error?.message,
                    )
                }
            },
            onError = { exception ->
                updateState {
                    copy(
                        error = exception.message,
                        isLoading = false
                    )
                }
            }
        )
    }

    fun loadNextPage() {
        viewModelScope.launch {
            pager.load()
        }
    }
}

sealed class ProductsEffect {
    object NavigateToAddDukanScreen : ProductsEffect()
    object NavigateToPendingDukanScreen : ProductsEffect()
}