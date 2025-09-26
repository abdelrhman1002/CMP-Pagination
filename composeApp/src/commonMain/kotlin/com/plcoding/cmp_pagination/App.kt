package com.plcoding.cmp_pagination

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.plcoding.cmp_pagination.pagination.PagingData
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
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
            modifier = Modifier.fillMaxSize()
        ) { contentPadding ->
            PaginatedList(
                state = state.movies,
                loadNextPage = { viewModel.loadNextPage() },
                itemContent = { movie ->
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
            )
        }
    }
}

@Composable
fun <T : Any> PaginatedList(
    state: PagingData<T>,
    listState: LazyListState = rememberLazyListState(),
    loadNextPage: () -> Unit,
    itemContent: @Composable (T) -> Unit
) {
    LazyColumn(state = listState) {
        items(state.items) { item ->
            itemContent(item)
        }

        if (state.isLoading) {
            item {
                CircularProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
        }
    }

    listState.OnLoadMore {
        if (state.hasMore && !state.isLoading) loadNextPage()
    }
}

@Composable
fun LazyListState.OnLoadMore(
    buffer: Int = 3,
    onLoadMore: () -> Unit
) {
    val scope = rememberCoroutineScope()
    scope.launch {
        snapshotFlow {
            val total = layoutInfo.totalItemsCount
            val lastVisible = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            lastVisible >= total - buffer
        }
            .distinctUntilChanged()
            .collect { shouldLoadMore ->
                if (shouldLoadMore) onLoadMore()
            }
    }
}
