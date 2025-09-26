package com.plcoding.cmp_pagination

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.ensureActive
import kotlin.coroutines.coroutineContext

class ProductsApi(
    private val httpClient: HttpClient
) {

    private companion object {
        const val API_KEY = "95e37f29931c77230c50c9fd824782b1"
        const val BASE_URL = "https://api.themoviedb.org/3"
    }

    suspend fun getProducts(
        page: Int = 1,
    ): Result<MovieResponse> {
        val body = try {
            val response = httpClient.get(
                urlString = "$BASE_URL/trending/movie/day"
            ) {
                contentType(ContentType.Application.Json)
                parameter("api_key", API_KEY)
                parameter("page", page)
                parameter("language", "en-US")
            }

            response.body<MovieResponse>()
        } catch(e: Exception) {
            coroutineContext.ensureActive()
            return Result.failure(e)
        }

        return Result.success(body)
    }
}