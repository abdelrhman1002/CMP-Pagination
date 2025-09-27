package com.plcoding.cmp_pagination

class GetAllProductsUseCase() {

    suspend operator fun invoke(page: Int): ProductsResponse =
        ProductsApi().getProducts(page = page).getOrThrow()
}