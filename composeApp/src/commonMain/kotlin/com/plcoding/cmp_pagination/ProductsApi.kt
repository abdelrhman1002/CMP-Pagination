package com.plcoding.cmp_pagination

import kotlinx.coroutines.delay

class ProductsApi {

    suspend fun getProducts(page: Int = 0): Result<ProductsResponse> {
        delay(500)

        val zeroBasedPage = if (page > 0) page - 1 else 0
        val pageSize = 10
        val totalElements = 50
        val totalPages = (totalElements + pageSize - 1) / pageSize // Ceiling division

        val startIndex = zeroBasedPage * pageSize + 1
        val endIndex = minOf(startIndex + pageSize - 1, totalElements)

        val products = (startIndex..endIndex).map { index ->
            Product(
                id = "product-id-$index",
                name = "Product $index",
                shelfId = "shelf-id",
                dukanId = "dukan-id",
                price = (10..100).random().toDouble(),
                description = "Description for product $index",
                imageUrls = listOf("https://picsum.photos/200/200?random=$index"),
                createdAt = "2025-09-26T18:26:41.300823"
            )
        }

        val response = ProductsResponse(
            totalElements = totalElements,
            totalPages = totalPages,
            first = zeroBasedPage == 0,
            last = zeroBasedPage >= totalPages - 1,
            size = pageSize,
            content = products,
            number = zeroBasedPage,
            sort = Sort(empty = false, sorted = true, unsorted = false),
            numberOfElements = products.size,
            pageable = Pageable(
                pageNumber = zeroBasedPage,
                pageSize = pageSize,
                offset = zeroBasedPage * pageSize,
                paged = true,
                unpaged = false,
                sort = Sort(empty = false, sorted = true, unsorted = false)
            ),
            empty = products.isEmpty()
        )

        return Result.success(response)
    }
}