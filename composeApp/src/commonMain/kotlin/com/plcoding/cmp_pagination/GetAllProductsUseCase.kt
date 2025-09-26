package com.plcoding.cmp_pagination

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class GetAllProductsUseCase() {

    suspend operator fun invoke(page: Int): MoviesEntity {
       val res = ProductsApi(
            httpClient = HttpClient(
                engine = HttpClientEngineFactory().create()
            ) {
                install(Logging) {
                    logger = Logger.SIMPLE
                    level = LogLevel.ALL
                }
                install(ContentNegotiation) {
                    json(
                        json = Json { ignoreUnknownKeys = true }
                    )
                }
            }
        )
        return res.getProducts(page = page).getOrThrow().toEntity()
    }
}