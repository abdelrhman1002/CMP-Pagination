package com.plcoding.cmp_pagination

data class ProductsResponse(
    val totalElements: Int,
    val totalPages: Int,
    val first: Boolean,
    val last: Boolean,
    val size: Int,
    val content: List<Product>,
    val number: Int,
    val sort: Sort,
    val numberOfElements: Int,
    val pageable: Pageable,
    val empty: Boolean
)

data class Product(
    val id: String,
    val name: String,
    val shelfId: String,
    val dukanId: String,
    val price: Double,
    val description: String,
    val imageUrls: List<String>,
    val createdAt: String
)

data class Sort(
    val empty: Boolean,
    val sorted: Boolean,
    val unsorted: Boolean
)

data class Pageable(
    val pageNumber: Int,
    val pageSize: Int,
    val offset: Int,
    val paged: Boolean,
    val unpaged: Boolean,
    val sort: Sort
)
