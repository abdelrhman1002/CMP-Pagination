package com.plcoding.cmp_pagination

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
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
import com.plcoding.cmp_pagination.pagination.Pager
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
fun App() {
    MaterialTheme {
        val viewModel = viewModel {
            ProductsViewModel(
                getAllProductsUseCase = GetAllProductsUseCase(),
            )
        }
        val state by viewModel.state.collectAsStateWithLifecycle()

        Scaffold(
            modifier = Modifier.fillMaxSize()
        ) { contentPadding ->
            val lazyListState = rememberLazyListState()

            lazyListState.LoadMoreOnScroll(
                pager = viewModel.pager,
                loadNextPage = { viewModel.loadNextPage() },
            )

            LazyColumn(
                state = lazyListState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = contentPadding
            ) {
                // Show initial loading state
                if (state.movies.items.isEmpty() && state.movies.isLoading) {
                    item {
                        Box(
                            modifier = Modifier.fillMaxWidth().height(200.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                } else {
                    itemsIndexed(state.movies.items) { index, movie ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(
                                text = movie.name,
                                fontSize = 18.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Total Items: ${state.movies.items.size}"
                                )
                                Text(
                                    text = "Index: $index"
                                )
                            }
                        }
                    }

                    if (state.movies.isLoading && state.movies.items.isNotEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }
                }

                // Show error state
                state.movies.error?.let { error ->
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Error: ${error.message}",
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun <T : Any> LazyListState.LoadMoreOnScroll(
    pager: Pager<Int, T>,
    loadNextPage: suspend () -> Unit,
) {
    LaunchedEffect(this) {
        snapshotFlow {
            val layoutInfo = layoutInfo
            val totalItems = layoutInfo.totalItemsCount
            val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0

            lastVisibleItemIndex to totalItems
        }.distinctUntilChanged()
            .collect { (lastVisible, total) ->
                if (pager.shouldLoadMore(lastVisible, total)) {
                    loadNextPage()
                }
            }
    }
}