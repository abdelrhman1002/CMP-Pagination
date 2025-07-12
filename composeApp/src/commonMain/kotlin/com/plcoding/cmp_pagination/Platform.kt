package com.plcoding.cmp_pagination

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform