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
import com.plcoding.cmp_pagination.pagination.Pager
import kotlinx.coroutines.flow.distinctUntilChanged
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        val viewModel = ProductsViewModel(getAllProductsUseCase = GetAllProductsUseCase())
        val state by viewModel.state.collectAsStateWithLifecycle()

        Scaffold(
            modifier = Modifier
                .fillMaxSize()
        ) { contentPadding ->
            val lazyListState = rememberLazyListState()

            lazyListState.LoadMoreOnScroll(pager = viewModel.pager) {
                viewModel.loadNextPage()
            }

            LazyColumn(
                state = lazyListState,
                modifier = Modifier
                    .fillMaxSize(),
                contentPadding = contentPadding
            ) {
                items(state.movies.items) { product ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = product.name,
                            fontSize = 18.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "$ ${product.price}"
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
        }
            .distinctUntilChanged()
            .collect { (lastVisible, total) ->
                if (pager.shouldLoadMore(lastVisible, total)) {
                    loadNextPage()
                }
            }
    }
}
