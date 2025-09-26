package com.plcoding.cmp_pagination

import androidx.lifecycle.viewModelScope
import com.plcoding.cmp_pagination.pagination.PagedFetchResponse
import com.plcoding.cmp_pagination.pagination.PagingData
import com.plcoding.cmp_pagination.pagination.createPagingSource
import kotlinx.coroutines.launch

data class ProductsState(
    val movies : PagingData<MovieEntity> = PagingData(),
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
                        movies = pagingData,
                    )
                }
            },
            onError = { exception ->
                updateState {
                    copy(
                        movies = movies.copy(
                            error = exception
                        )
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