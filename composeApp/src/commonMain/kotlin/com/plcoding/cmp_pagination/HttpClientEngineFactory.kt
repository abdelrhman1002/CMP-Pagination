package com.plcoding.cmp_pagination

import io.ktor.client.engine.HttpClientEngine

expect class HttpClientEngineFactory() {
    fun create(): HttpClientEngine
}