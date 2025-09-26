package com.plcoding.cmp_pagination


data class MovieEntity(
    val id: Int,
    val overview: String,
    val posterPath: String,
    val releaseDate: String,
)

data class MoviesEntity(
    val page: Int,
    val movieDetailApi: List<MovieEntity> = listOf(),
    val totalPages: Int,
)