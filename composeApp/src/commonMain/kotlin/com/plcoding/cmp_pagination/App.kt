package com.plcoding.cmp_pagination

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
fun App() {
    MaterialTheme {
        val viewModel = viewModel(
            initializer = {
                ProductsViewModel(
                    getAllProductsUseCase = GetAllProductsUseCase(),
                )
            }
        )
        val state by viewModel.state.collectAsStateWithLifecycle()

        Scaffold(
            modifier = Modifier
                .fillMaxSize()
        ) { contentPadding ->
            val lazyListState = rememberLazyListState()

            lazyListState.LoadMoreOnScroll(
                hasMore = state.movies.hasMore,
                isLoading = state.movies.isLoading,
                loadNextPage = { viewModel.loadNextPage() },
            )

            LazyColumn(
                state = lazyListState,
                modifier = Modifier
                    .fillMaxSize(),
                contentPadding = contentPadding
            ) {
                items(state.movies.items) { movie ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = movie.overview,
                            fontSize = 18.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "$ ${movie.releaseDate}"
                        )
                    }
                }

                if(state.movies.isLoading) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }
        }
    }
}



@Composable
fun  LazyListState.LoadMoreOnScroll(
    hasMore: Boolean,
    isLoading: Boolean,
    loadNextPage: suspend () -> Unit,
    buffer: Int = 0
) {
    LaunchedEffect(this) {
        snapshotFlow {
            val layoutInfo = this@LoadMoreOnScroll.layoutInfo
            val totalItems = layoutInfo.totalItemsCount
            val lastVisibleIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            lastVisibleIndex >= totalItems - buffer
        }
            .distinctUntilChanged()
            .collect { shouldLoadMore ->
                if (shouldLoadMore && hasMore && !isLoading) {
                    loadNextPage()
                }
            }
    }
}
